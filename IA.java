package mainfdgs; 

public class IA {
	public IA(){
	}
	
	public static char bestMove(char couleurPlayer){ 
	//de la forme {'R','V','B','I','J','O'}
	int[] couleursAutour=new int[6]; 
	
	//on compte le nombre de couleurs autour des positions du joueur
	for (int i=0; i<Main.board.len;i++){
		for (int j=0; j<Main.board.len;j++){
			
			for (int i1=-1; i1<2; i1++){
				for (int j1=-1; j1<2; j1++){
					try{
						if(Main.board.grille[i+i1][j+j1]==couleurPlayer &&i!=i1 &&j!=j1){
							switch(Main.board.grille[i][j]){
							case 'r': 
								couleursAutour[0]++; 
								break; 
							case 'v': 
								couleursAutour[1]++; 
								break; 
							case 'b': 
								couleursAutour[2]++; 
								break; 
							case 'i': 
								couleursAutour[3]++; 
								break; 
							case 'j': 
								couleursAutour[4]++; 
								break; 
							case 'o': 
								couleursAutour[5]++; 
								break; 	
							}
						}
						//permet de gérer les bords de la grille
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
		}
	}
	return maximum(couleursAutour); 
	}
	
	public static char maximum(int[] tableau){
		//on cherche le maximum du tableau 
		
		char[] couleurs={'R','V','B','I','J','O'}; 
		int max=-1; 
		int indice_max=-1; 
		
		for (int m=0; m<6; m++){
			if (tableau[m]>max && Main.choixCouleur(couleurs[m], Main.courantPlayer)){
				max=tableau[m]; 
				indice_max=m; 
			}
		}
		return couleurs[indice_max]; 
	}
}
