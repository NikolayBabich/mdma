package ru.filit.mdma.dm.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import ru.filit.mdma.dm.Application;

public final class FileUtil {

  private static final File currentRunDirectory = new ApplicationHome(Application.class).getDir();

  private FileUtil() {
    throw new UnsupportedOperationException("Utility class is not instantiable");
  }

  public static File copyOutsideOfJar(String fileName) {
    try {
      ClassPathResource classPathResource = new ClassPathResource(fileName);
      URL resourceUrl = classPathResource.getURL();
      if (resourceUrl.getPath().contains("jar")) {
        File outsideFile = FileUtils.getFile(currentRunDirectory, fileName);
        FileUtils.copyURLToFile(resourceUrl, outsideFile);
        return outsideFile;
      }
      return classPathResource.getFile();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
