package sailpoint.services.tools.docgen;

import java.io.File;
import sailpoint.services.tools.docgen.Documentizer;

public class DocumentGeneratorCL {
    private static File src;
    private static File dest;

    public static void main(String[] args) {
        src = new File(args[0]);
        dest = new File(args[1]);
        
        Documentizer d = new Documentizer(src, dest);
        
        d.execute();
    }
}