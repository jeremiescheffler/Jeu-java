package mainfdgs;

public class Joueurs {

	public char couleur = ' ';
	
	private int num;
	private static int nombrePlayer = 0;
	private static Board b;
	
	public Joueurs (int n, char coul, Board b){
		nombrePlayer++;
		
		this.num = n;
		this.couleur = coul;
		Joueurs.b = b;
		
		if(Main.typeGrille == 'R'){
			positionNormale();
		}else{
			positionLosange();
		}
		
		
	}
	
	public static int getNombrePlayer(){
		return nombrePlayer;
	}

	public void positionNormale(){
		switch(this.num){
		//position de depart des joueurs (dans les coins)
			case 1:
				b.grille[0][0] = this.couleur;			    
				break;
			case 2:
				b.grille[b.len-1][b.len -1] = this.couleur;
				break;
			case 3:
				b.grille[b.len-1][0] = this.couleur;
				break;
			case 4:
				b.grille[0][b.len-1] = this.couleur;
				break;
		}
	}
	
	public void positionLosange(){
		switch(this.num){
		//position de depart des joueurs (dans les coins)
			case 1:
				b.grille[12][0] = this.couleur;			    
				break;
			case 2:
				b.grille[12][24] = this.couleur;
				break;
			case 3:
				b.grille[0][12] = this.couleur;
				break;
			case 4:
				b.grille[24][12] = this.couleur;
				break;
		}
	}
}
