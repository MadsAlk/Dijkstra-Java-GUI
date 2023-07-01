package application;

public class Edge {
private int num;	
private double weight;
private Edge next;

public int getNum() {
	return num;
}
public void setNum(int num) {
	this.num = num;
}
public double getWeight() {
	return weight;
}
public void setWeight(double weight) {
	this.weight = weight;
}
public Edge getNext() {
	return next;
}
public void setNext(Edge next) {
	this.next = next;
}
public Edge(int num, double weight) {
	super();
	this.num = num;
	this.weight = weight;
}




}
