package pojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by kohlih on 11-11-2017.
 */
@XmlRootElement(name = "User")
public class User
{
    String id;

    String lastName;

    String phone;

    String username;

    String email;

    String userStatus;

    String firstName;

    String password;

    public String getId ()
    {
        return id;
    }

    @XmlElement
    public void setId (String id)
    {
        this.id = id;
    }

    public String getLastName ()
    {
        return lastName;
    }

    @XmlElement
    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getPhone ()
    {
        return phone;
    }

    @XmlElement
    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getUsername ()
    {
        return username;
    }

    @XmlElement
    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getEmail ()
    {
        return email;
    }

    @XmlElement
    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getUserStatus ()
    {
        return userStatus;
    }

    @XmlElement
    public void setUserStatus (String userStatus)
    {
        this.userStatus = userStatus;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    @XmlElement
    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getPassword ()
    {
        return password;
    }

    @XmlElement
    public void setPassword (String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", lastName = "+lastName+", phone = "+phone+", username = "+username+", email = "+email+", userStatus = "+userStatus+", firstName = "+firstName+", password = "+password+"]";
    }
}