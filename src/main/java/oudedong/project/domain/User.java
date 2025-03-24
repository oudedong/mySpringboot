package oudedong.project.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oudedong.project.vo.AuthorityType;


@Entity
@Table(name = "userTable")
@Getter
@Setter
@NoArgsConstructor
public class User{

    @Id
    @Column(name="id", updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(name="username", nullable = false, unique = true)
    String username;

    @Column(name="password",nullable = false)
    String password;

    @ElementCollection
    @CollectionTable(name = "User_Authorities", joinColumns = @JoinColumn(name = "id"))
    Set<AuthorityType> authorities = new HashSet<>();

    public void addAuthority(AuthorityType authority){
        authorities.add(authority);
    }
}
