package kr.netty;

import java.util.Date;

public class Task
{
	public int id;
	public String city;
	public String temperature;
	public Date data;

	public Task(int id,String city)
	{
		this.id=id;
		this.city=city;
		this.data=new Date();
		this.temperature="0";

	}
}