/**
 *
 * @author Brhaka - Eduardo J Martinez
 */

package bksql;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Useful {
    protected static Boolean IsNullOrEmpty(String str) {
        if(str != null && !str.isEmpty()) {
            return false;
        }
        return true;
    }

    protected static Boolean IsNotNullOrEmpty(String str) {
        if(str != null && !str.isEmpty()) {
            return true;
        }
        return false;
    }

    protected static void DeleteEntireDirectory(String pathString) {
        Path path = Paths.get(pathString);

        if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    DeleteEntireDirectory(entry.toString());
                }
            } catch (IOException ex) {
                Logger.getLogger(Useful.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            Files.delete(path);
        } catch (IOException ex) {
            Logger.getLogger(Useful.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
