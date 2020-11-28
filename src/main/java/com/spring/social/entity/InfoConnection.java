package com.spring.social.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name = "InfoConnection")
public class InfoConnection {

	@Id
	@GeneratedValue
	@Column(name = "ConnectionId", nullable = false)
	private Long connectionid;

	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "User_Id", unique=false, nullable=false, updatable=false)
	@Column(name = "UserId", nullable = false)
	private Long userid;
	
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Login_Date")
	private Date Login_Date;
	
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Logout_Date")
	private Date Logout_Date;

	public Long getConnectionId() {
		return connectionid;
	}

	public void setConnectionId(Long connectionid) {
		this.connectionid = connectionid;
	}

	public Date getLogin_Date() {
		return Login_Date;
	}

	public void setLogin_Date(Date Login_Date) {
		this.Login_Date = Login_Date;
	}

	public Date getLogout_Date() {
		return Logout_Date;
	}

	public void setLogout_Date(Date logout_Date) {
		Logout_Date = logout_Date;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

}