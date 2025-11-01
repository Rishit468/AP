package Database;

import org.mindrot.jbcrypt.BCrypt;
public class SeedGenerate {
    public static void main(String[] args) {
        // Define username:password pairs
        String[][] accounts = {
                {"admin1","Admin@123"},
                {"inst1","Inst@123"},
                {"stu1","Stu1@123"},
                {"stu2","Stu2@123"}
        };
        for (String[] a : accounts) {
            String user = a[0];
            String pass = a[1];
            String hash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
            System.out.println(String.format(
                    "INSERT INTO auth_db.users (username, role, pass_hash, status) VALUES ('%s','%s','%s','ACTIVE');",
                    user,
                    user.startsWith("admin") ? "ADMIN" : user.startsWith("inst") ? "INSTRUCTOR" : "STUDENT",
                    hash
            ));
        }
    }
}
