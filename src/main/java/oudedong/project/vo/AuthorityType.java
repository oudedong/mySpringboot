package oudedong.project.vo;

public enum AuthorityType {

    ROLE_ADMIN(0),
    ROLE_USER(1),
    ROLE_VISITOR(2);

    public final int priority;

    AuthorityType(int priority){
        this.priority = priority;
    }
}
