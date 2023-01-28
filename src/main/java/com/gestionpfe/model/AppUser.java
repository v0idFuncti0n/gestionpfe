package com.gestionpfe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    private String department;

    @OneToMany(mappedBy="supervisor")
    @ToString.Exclude
    private List<PFESubject> pfeSubjects;

    //Student fields
    private String codeApogee;
    private String branch;

    @ManyToMany(mappedBy = "students", cascade = {CascadeType.ALL})
    @ToString.Exclude
    private List<StudentGroup> studentGroup;

    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private Boolean locked;
    private Boolean enabled;

    public AppUser(String firstName, String lastName, String email, String password, String department, AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = false;
    }

    public AppUser(String firstName, String lastName, String email, String password, String department, AppUserRole appUserRole, boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.appUserRole = appUserRole;
        this.locked = false;
        this.enabled = enabled;
    }

    public AppUser(String codeApogee, String firstName, String lastName, String email, String password, String branch, AppUserRole appUserRole) {
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

    public AppUser(String codeApogee, String firstName, String lastName, String email, String password, String branch, AppUserRole appUserRole, boolean enabled) {
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
}
