package codigo.labplc.mx.mitaxi.beans;

public class User {
	private String name;
	private String lastname;
	private String email;
	private String teluser;
	private String telemergency;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTeluser() {
		return teluser;
	}
	public void setTeluser(String teluser) {
		this.teluser = teluser;
	}
	public String getTelemergency() {
		return telemergency;
	}
	public void setTelemergency(String telemergency) {
		this.telemergency = telemergency;
	}
}