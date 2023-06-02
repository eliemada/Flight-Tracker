package ch.epfl.javions.gui;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Elie BRUNO (elie.bruno@epfl.ch)
 * The TileManager class provides methods for managing and caching tile images for a given tile server URL.
 */
public final class TileManager {
    /**
     * The size of each tile image.
     */
    public final static int TILE_SIZE = 256;
    /**
     * the suffix of each tile image file name.
     */
    private final static String IMAGE_SUFFIX = ".png";
    /**
     * The prefix of each tile server URL.
     */
    private final static String URL_PREFIX = "https://";
    /**
     * The path delimiter used in tile server URLs.
     */
    private final static String PATH_DELIMITER = "/";
    /**
     * The path to the directory to use for caching tile images.
     */
    private final Path cacheDir;
    /**
     * The URL of the tile server to use for downloading tile images.
     */
    private final String tileServerUrl;
    /**
     *
     */
    private final Map<TileId, Image> memoryCache;

    /**
     * Constructs a TileManager instance.
     * Initializes the memory cache with a capacity of 100 entries.
     *
     * @param cacheDir      the path to the directory to use for caching tile images
     * @param tileServerUrl the URL of the tile server to use for downloading tile images
     */
    public TileManager(Path cacheDir, String tileServerUrl) {
        this.cacheDir = cacheDir;
        this.tileServerUrl = tileServerUrl;
        // The LinkedHashMap constructor takes a boolean parameter that specifies whether the
        // access-order should be used. If true, the least recently accessed entry is removed when
        // the cache is full. If false, the least recently inserted entry is removed.
        this.memoryCache = new LinkedHashMap<>(100, 0.75f, true);

    }


    /**
     * Returns the tile image for the given TileId.
     * First, it checks the in-memory cache. If the image is not found,
     * it then checks the local disk cache or downloads it from the tile server if necessary.
     *
     * @param tileId the TileId of the tile image to retrieve
     * @return the tile image for the given TileId, or null if the tile image could not be retrieved
     * @throws IOException if the tile image could not be retrieved
     */
    public Image imageForTileAt(TileId tileId) throws IOException {
        Image image = memoryCache.get(tileId); // Check the memory cache first.
        if (image == null) { // If the tile image is in the memory cache, return it.
            return getTileFromDiskOrDownload(tileId);
        }
        maintainCacheSize();
        return image;
    }

    /**
     * Retrieves the tile image from disk or downloads it from the server if not already cached.
     * If successful, also adds the image to the memory cache.
     *
     * @param tileId the TileId of the tile image to retrieve
     * @return the tile image for the given TileId
     * @throws IOException if the tile image could not be retrieved
     */
    private Image getTileFromDiskOrDownload(TileId tileId) throws IOException {
        Path imagePath = buildImagePath(tileId);
        Image image;
        if (Files.exists(imagePath)) {
            image = loadImageFromDisk(imagePath);
        } else {
            image = downloadAndCacheImage(tileId, imagePath);
        }
        memoryCache.put(tileId, image);
        return image;
    }

    /**
     * Loads an image from disk given a disk path.
     *
     * @param diskPath the path of the image on disk
     * @return the Image object representing the loaded image
     * @throws IOException if there's an error loading the image
     */
    private Image loadImageFromDisk(Path diskPath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(diskPath)) {
            return new Image(inputStream);
        }
    }

    /**
     * Downloads an image from the server and caches it on disk.
     *
     * @param tileId   the TileId of the tile image to download
     * @param diskPath the disk path where the image is to be cached
     * @return the Image object representing the downloaded image
     * @throws IOException if there's an error downloading or caching the image
     */
    private Image downloadAndCacheImage(TileId tileId, Path diskPath) throws IOException {
        URL tileUrl = buildTileUrl(tileId);
        URLConnection urlConnection = tileUrl.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Javions");
        try (InputStream inputStream = urlConnection.getInputStream()) {
            byte[] tileBytes = inputStream.readAllBytes();
            cacheImageOnDisk(diskPath, tileBytes);
            return new Image(new ByteArrayInputStream(tileBytes));
        }
    }

    /**
     * Caches an image on disk given a disk path and the image bytes.
     *
     * @param diskPath  the disk path where the image is to be cached
     * @param tileBytes the bytes representing the image
     * @throws IOException if there's an error writing the image to disk
     */
    private void cacheImageOnDisk(Path diskPath, byte[] tileBytes) throws IOException {
        Files.createDirectories(diskPath.getParent());
        Files.write(diskPath, tileBytes);
    }

    /**
     * Builds an image path based on the tileId.
     *
     * @param tileId the TileId of the tile image
     * @return the built image path
     */
    private Path buildImagePath(TileId tileId) {
        return cacheDir
                .resolve(Integer.toString(tileId.zoomLevel()))
                .resolve(Integer.toString(tileId.x()))
                .resolve((tileId.y()) + IMAGE_SUFFIX);
    }

    /**
     * Builds a URL for a tile image given a TileId.
     *
     * @param tileId the TileId of the tile image to build the URL for
     * @return the built URL
     * @throws IOException if there's an error constructing the URL
     */
    private URL buildTileUrl(TileId tileId) throws IOException {
        String url = URL_PREFIX + tileServerUrl + PATH_DELIMITER + tileId.zoomLevel() + PATH_DELIMITER
                + tileId.x() + PATH_DELIMITER + tileId.y() + IMAGE_SUFFIX;
        return new URL(url);
    }

    /**
     * Checks and maintains the size of the memory cache.
     * Removes the least recently accessed entry if the cache size limit is reached.
     */
    private void maintainCacheSize() {
        if (memoryCache.size() >= 100) {
            memoryCache.remove(memoryCache.keySet().iterator().next());
        }
    }

    /**
     * The TileId record represents the unique identifier of a tile image.
     * It consists of a zoom level and x, y coordinates.
     * <p>
     * The static method isValid is used to validate the parameters of a TileId.
     *
     * @param zoomLevel the zoom level of the tile image
     * @param x         the x coordinate of the tile image
     * @param y         the y coordinate of the tile image
     */
    public record TileId(int zoomLevel, int x, int y) {
        public static boolean isValid(int zoomLevel, int x, int y) {
            int maxZoomLevel = (int) Math.scalb(1, zoomLevel);
            return zoomLevel >= 0 && x >= 0 && x < maxZoomLevel && y >= 0 && y < maxZoomLevel;
        }
    }
}

