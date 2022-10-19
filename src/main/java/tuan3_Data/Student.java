package tuan3_Data;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement 
@XmlType(propOrder = { "mssv", "hoten", "ngaysinh" })
public class Student implements Serializable {
	private long mssv;
	private String hoten;
	private Date ngaysinh;

	public Student(long mssv, String hoten, Date ngaysinh) {
		this.mssv = mssv;
		this.hoten = hoten;
		this.ngaysinh = ngaysinh;
	}

	public Student() {
	}

	public long getMssv() {
		return mssv;
	}

	public void setMssv(long mssv) {
		this.mssv = mssv;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public Date getNgaysinh() {
		return ngaysinh;
	}

	public void setNgaysinh(Date ngaysinh) {
		this.ngaysinh = ngaysinh;

	}

	@Override
	public String toString() {
		return mssv + "\t" + hoten + "\t" + ngaysinh;
	}
}