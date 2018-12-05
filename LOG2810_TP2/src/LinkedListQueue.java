// Ceci est une linkedlist (FIFO) personalise pour ce TD
//elle peux accepter seulement 5 etats les plus recents

public class LinkedListQueue
{	
	// Un noeud de la file
	@SuppressWarnings("hiding")
	private class Node
	{
		private Etat data;
		private Node next;

		public Node(Etat data, Node next)
		{
			this.data = data;
			this.next = next;
		}

		public void setNext(Node next)
		{
			this.next = next;
		}

		public Node getNext()
		{
			return next;
		}

	}
   
	private int size = 0;		//Nombre d'elements dans la file.
	private Node last;	//Dernier element de la liste
	

	public boolean empty() 
	{ 
		return size == 0; 
	}

	public int size() 
	{ 
		return size; 
	}

	//Retire l'element en tete de file
	public void pop(){
		 if ( size > 1){
		 	last.getNext().data.resetMostRecently();
			Node first = last.getNext().getNext();
			last.setNext(first);
			size--;
		}
		else {
			last = null;	
			size--;
		}
	}

	public void push(Etat item)
	{

		Node latest= new Node (item, null);
		if (item.isSetMostRecently()){
			Node tmp = last;
			while (tmp.getNext().data != item){
				tmp = tmp.getNext();
			}
			Node next = tmp.getNext();
			tmp.setNext(next.getNext());
			next.setNext(last.getNext());
			last.setNext(next);
			last = next;
			return;
		}
		item.setMostRecently();
		if (empty()) {
			last = latest;
			last.setNext(latest); 
		}
		else {
			if (size >= 5) {
				this.pop();
			}

			latest.setNext(last.getNext());
			last.setNext(latest);
			last = latest;
		}
		size++;
	}
}
