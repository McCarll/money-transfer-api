import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by vrudometkin on 05/12/2017.
 */
public class Utils {

    public static String readFile(String filePath) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(filePath);
        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return is;
            }
        };
        return byteSource.asCharSource(Charsets.UTF_8).read();
    }

}
