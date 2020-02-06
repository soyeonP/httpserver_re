package dispatcher;

import java.io.File;

public class WriteETag {

    public String getETag(File file) {
        return String.valueOf((file.getPath() + file.lastModified()).hashCode());
    }

}
