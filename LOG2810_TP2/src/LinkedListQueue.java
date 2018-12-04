
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
	
	//Indique si la file est vide
	public boolean empty() 
	{ 
		return size == 0; 
	}
	
	//Retourne la taille de la file
	public int size() 
	{ 
		return size; 
	}

	//Retire l'element en tete de file
	//complexit� asymptotique: O(1)
	public void pop(){
		 if ( size > 1){
			//le deuxieme element est maitenant considere comme le premier
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
			Node test = tmp.getNext();
			tmp.setNext(test.getNext());
			test.setNext(last.getNext());
			last.setNext(test);
			last = test;
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
