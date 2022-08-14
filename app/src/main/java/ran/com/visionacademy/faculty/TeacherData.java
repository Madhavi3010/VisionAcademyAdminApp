package ran.com.visionacademy.faculty;

public class TeacherData {
    private String name, email, subject, image, key;

    public TeacherData() {
    }

    public TeacherData(String name, String email, String subject, String image, String key) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.image = image;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
