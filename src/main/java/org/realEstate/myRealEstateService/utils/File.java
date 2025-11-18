package org.realEstate.myRealEstateService.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class File {
    private List<String> allowedExt = List.of("jpg", "jpeg", "png", "gif");
    private List<String> allowedMime = List.of("image/jpeg", "image/png", "image/gif");

    public String getFileExt(MultipartFile file) {
        if (file != null && file.getOriginalFilename() != null) {
            return FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        }
        return ".na";
    }

    public void validateFile(MultipartFile file) throws CustomException, IOException {
        // extension
        String ext = getFileExt(file);
        if (!allowedExt.contains(ext)) {
            throw new CustomException("Unsupported file extension");
        }

        // mime type
        if (!allowedMime.contains(file.getContentType())) {
            throw new CustomException("Unsupported MIME type");
        }

        // file signature
        Tika tika = new Tika();
        String mime = tika.detect(file.getInputStream());
        if (mime == null || !mime.startsWith("image/")) {
            throw new CustomException("File content does not match an image");
        }

        // size
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new CustomException("File too large");
        }
    }

    // file.getOriginalFilename()
    public String getFilename(String ext) {
        return UUID.randomUUID() + "." + ext;
    }

    public void createDir(String dir) throws IOException {
        Files.createDirectories(Paths.get(dir));
    }

    public void copyFile(InputStream stream, Path path) throws IOException {
        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteFile(Path pathAndFilename) throws IOException {
        Files.deleteIfExists(pathAndFilename);
    }
}
