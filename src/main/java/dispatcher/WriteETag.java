package dispatcher;

import java.io.File;

public class WriteETag {

    public String getETag(File file, String resource) {
        return String.valueOf((resource + file.lastModified()).hashCode());
    }

}
