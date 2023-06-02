package ch.epfl.cs108;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.net.HttpURLConnection.*;

public final class Submit {
    // CONFIGURATION
    // -------------
    // Jeton du premier membre du groupe
    private static final String TOKEN_1 = "eeChahs1";
    // Jeton du second membre (identique au premier pour les personnes travaillant seules)
    private static final String TOKEN_2 = "eeChahs1";
    // -------------

    private static final String ZIP_ENTRY_NAME_PREFIX = "Javions/";
    private static final int TOKEN_LENGTH = 8;
    private static final int TIMEOUT_SECONDS = 5;

    private static final LocalDate SEMESTER_START_DATE = LocalDate.of(2023, Month.FEBRUARY, 20);
    private static final URI baseUri = URI.create("https://cs108.epfl.ch/");

    private static final String BASE32_ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ";

    private static final Pattern SUBMISSION_ID_RX =
            Pattern.compile(
                    Stream.generate(() -> "[%s]{4}".formatted(BASE32_ALPHABET))
                            .limit(4)
                            .collect(Collectors.joining("-")));

    public static void main(String[] args) {
        var token1 = args.length >= 1 ? args[0] : TOKEN_1;
        var token2 = args.length >= 2 ? args[1] : TOKEN_2;

        if (token1.length() != TOKEN_LENGTH) {
            System.err.println("Erreur : vous n'avez correctement défini TOKEN_1 dans Submit.java !");
            System.exit(1);
        }
        if (token2.length() != TOKEN_LENGTH) {
            System.err.println("Erreur : vous n'avez correctement défini TOKEN_2 dans Submit.java !");
            System.exit(1);
        }

        try {
            var projectRoot = Path.of(System.getProperty("user.dir"));
            var submissionTimeStamp = LocalDateTime.now();

            var submissionsDir = projectRoot.resolve("submissions");
            if (!Files.isDirectory(submissionsDir)) {
                try {
                    Files.createDirectory(submissionsDir);
                } catch (FileAlreadyExistsException e) {
                    System.err.printf("Erreur : impossible de créer le dossier %s !\n", submissionsDir);
                }
            }

            var fileList = getFileList(semesterWeek(LocalDate.now()));
            var paths = filesToSubmit(projectRoot, p -> fileList.stream().anyMatch(p::endsWith));

            var zipArchive = createZipArchive(paths);
            var backupName = "%tF_%tH%tM%tS".formatted(
                    submissionTimeStamp, submissionTimeStamp, submissionTimeStamp, submissionTimeStamp);
            var backupPath = submissionsDir.resolve(backupName + ".zip");
            writeZip(backupPath, zipArchive);

            var response = submitZip(token1 + token2, zipArchive);
            var wasCreated = response.statusCode() == HTTP_CREATED;
            var printStream = wasCreated ? System.out : System.err;
            switch (response.statusCode()) {
                case HTTP_CREATED -> {
                    var subIdMatcher = SUBMISSION_ID_RX.matcher(response.body());
                    var subId = subIdMatcher.find() ? subIdMatcher.group() : "ERREUR";
                    var oldBackupPath = backupPath;
                    backupPath = submissionsDir.resolve(backupName + "_" + subId + ".zip");
                    Files.move(oldBackupPath, backupPath);
                    printStream.printf("""
                                    Votre rendu a bien été reçu par le serveur et stocké sous le nom :
                                      %s
                                    Il est composé des fichiers suivants :
                                      %s
                                    Votre rendu sera prochainement validé et le résultat de cette
                                    validation vous sera communiqué par e-mail, à votre adresse de l'EPFL.
                                    """,
                            subId,
                            paths.stream().map(Object::toString).collect(Collectors.joining("\n  ")));
                }
                case HTTP_ENTITY_TOO_LARGE -> printStream.println("Erreur : l'archive est trop volumineuse !");
                case HTTP_UNAUTHORIZED -> printStream.println("Erreur : au moins un des jetons est invalide !");
                case HTTP_BAD_GATEWAY -> printStream.println("Erreur : le serveur de rendu n'est pas actif !");
                default -> printStream.printf("Erreur : réponse inattendue (%s)", response);
            }
            printStream.printf("\nUne copie de sauvegarde de l'archive a été stockée dans :\n  %s\n",
                    projectRoot.relativize(backupPath));
            System.exit(wasCreated ? 0 : 1);
        } catch (IOException | InterruptedException e) {
            System.err.println("Erreur inattendue !");
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static int semesterWeek(LocalDate date) {
        assert SEMESTER_START_DATE.isBefore(date);
        return 1 + (int) ChronoUnit.WEEKS.between(SEMESTER_START_DATE, date);
    }

    private static List<Path> getFileList(int stage) throws IOException, InterruptedException {
        var fileListUri = baseUri.resolve("p/f/files-to-submit-%02d.txt".formatted(stage));
        var httpRequest = HttpRequest.newBuilder(fileListUri)
                .GET()
                .build();
        return HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofLines())
                .body()
                .map(Path::of)
                .toList();
    }

    private static List<Path> filesToSubmit(Path projectRoot, Predicate<Path> keepFile) throws IOException {
        try (var paths = Files.walk(projectRoot)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(projectRoot::relativize)
                    .filter(keepFile)
                    .sorted(Comparator.comparing(Path::toString))
                    .toList();
        }
    }

    private static byte[] createZipArchive(List<Path> paths) throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        try (var zipStream = new ZipOutputStream(byteArrayOutputStream)) {
            for (var path : paths) {
                var entryPath = IntStream.range(0, path.getNameCount())
                        .mapToObj(path::getName)
                        .map(Path::toString)
                        .collect(Collectors.joining("/", ZIP_ENTRY_NAME_PREFIX, ""));
                zipStream.putNextEntry(new ZipEntry(entryPath));
                try (var fileStream = new FileInputStream(path.toFile())) {
                    fileStream.transferTo(zipStream);
                }
                zipStream.closeEntry();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    private static HttpResponse<String> submitZip(String submissionToken, byte[] zipArchive)
            throws IOException, InterruptedException {
        var httpRequest = HttpRequest.newBuilder(baseUri.resolve("api/submissions"))
                .POST(HttpRequest.BodyPublishers.ofByteArray(zipArchive))
                .header("Authorization", "token %s".formatted(submissionToken))
                .header("Content-Type", "application/zip")
                .header("Accept", "text/plain")
                .header("Accept-Language", "fr")
                .timeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();

        return HttpClient.newHttpClient()
                .send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    private static void writeZip(Path filePath, byte[] zipArchive) throws IOException {
        try (var c = new FileOutputStream(filePath.toFile())) {
            c.write(zipArchive);
        }
    }
}
