package org.realEstate.myRealEstateService.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.Tika;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class File {
    private List<String> allowedExt = List.of("jpg", "jpeg", "png", "gif");
    private List<String> allowedMime = List.of("image/jpeg", "image/png", "image/gif");

    public void validateFile(MultipartFile file) throws CustomException, IOException {
        // extension
        String ext = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
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

    public String getFilename(String originalFilename, int counter) {
        return System.currentTimeMillis() + counter + "_" + originalFilename;
    }
}
