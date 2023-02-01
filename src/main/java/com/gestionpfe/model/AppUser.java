package com.gestionpfe.model;

import com.gestionpfe.enums.AppUserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // Supervisor fields

    @OneToMany(mappedBy="supervisor")
    @ToString.Exclude
    private List<PFESubject> pfeSubjects;

    @ManyToOne
    private Department department;

    //Student fields
    private String codeApogee;
    @ManyToOne
    private Branch branch;

    @ManyToMany(mappedBy = "students", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<StudentGroup> studentGroup;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private Boolean locked;
    private Boolean enabled;

    public AppUser(String firstName, String lastName, String email, String password, Department department, AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = false;
    }

    public AppUser(String firstName, String lastName, String email, String password, Department department, AppUserRole appUserRole, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = enabled;
    }

    public AppUser(String codeApogee, String firstName, String lastName, String email, String password, Branch branch, AppUserRole appUserRole) {
        this.codeApogee = codeApogee;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.branch = branch;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = false;
    }

    public AppUser(String codeApogee, String firstName, String lastName, String email, String password, Branch branch, AppUserRole appUserRole, boolean enabled) {
        this.codeApogee = codeApogee;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.branch = branch;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
