package mainfdgs;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    
    //3 variables modifiable (jusqu'a 4 joueurs)
    public static int tailleGrille;
    public static char typeGrille;
    public static int nombrePlayer;
    
    public static int[] listeIA = new int[4];
    private static int difficulty = 0;
    
    public static int courantPlayer = 0;
    public static char[] couleursPlayer = new char[4];
    private static boolean partieTerminee = false;
    
    public static Board board;
    public static Joueurs J1;
    public static Joueurs J2;
    public static Joueurs J3;
    public static Joueurs J4;
    
    public static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("===== Jeu des 6 couleurs ====");
        
        //CHOIX DU NOMBRE DE JOUEURS
        do{
            System.out.println("A combien voulez vous jouer? (4 max)");
            nombrePlayer = sc.nextInt();
        }while(0 >= nombrePlayer || nombrePlayer >4);
        sc.nextLine();
        
        //AJOUT D'UNE IA
        char r;
        System.out.println("Ajouter une IA (<O>ui, <N>on)");
        r = sc.nextLine().toUpperCase().charAt(0);
        if(r == 'O'){
            try{
                //CHOIX DE LA DIFFICULTE
                System.out.println("Avec quelle difficulté voulez vous jouer ?\n(1:facile, 2:difficile)");
                difficulty = sc.nextInt();
            
                //CHOIX DES IA
                for(int i=0; i<nombrePlayer-1; i++){
                    System.out.println("Mettre une IA sur quel joueur ? (N pour quitter)");
                    int x = sc.nextInt();
                    listeIA[x-1] = x;
                    
                }
            }catch(ArrayIndexOutOfBoundsException e1){}
            catch(InputMismatchException e2){}
            sc.nextLine();
        }
        
        //CHOIX DE LA FORME DE LA GRILLE
        do{
            System.out.println("Sur quelle grille ? (<R>ectangle, <L>osange)");
            typeGrille = sc.nextLine().toUpperCase().charAt(0);
        }while(typeGrille != 'R' && typeGrille !='L');
        
        if(typeGrille == 'R'){
            System.out.println("Sur une grille de quelle taille?");
            tailleGrille = sc.nextInt();
            board = new Board("rectangle", tailleGrille);
            sc.nextLine(); //on vide la ligne
        }else{
            tailleGrille = 25;
            board = new Board("losange", tailleGrille);
        }
        
        //on modifie les paramètres des joueurs
        ajoutPlayer();
        
        //BOUCLE DE JEU :
        //tant que la partie n'est pas terminée
        //isFinish() dans la classe Board
        while(partieTerminee == false){
            char coulSelect = ' ';
            
            //on affiche le jeu
            board.afficher();
            
            //on incrémente la variable courantPlayer
            joueursSuivant();
            
            //tant que la couleur n'est pas valable on redemande
            //si l'utilisateur ne rentre aucun lettre on demande si il veut quitter
            try{
                
                do{
                    if(!isIn(courantPlayer, listeIA)){
                        System.out.println("Joueur "+courantPlayer+" a votre tour :");
                        coulSelect = sc.nextLine().toUpperCase().charAt(0);
                    }
                    else{
                        if(difficulty == 1){
                            System.out.println("IA "+courantPlayer+" joue");
                            char[] all = {'R','B','J','V','I','O'};
                            coulSelect = all[(int) (Math.random()*6)]; 
                        }
                        else{
                            coulSelect = IA.bestMove(couleursPlayer[courantPlayer-1]);
                        }
                    }
                }while(!choixCouleur(coulSelect, courantPlayer) );
                
            }catch(StringIndexOutOfBoundsException e){
                System.out.println("(Tapez sur entrée a nouveau pour quitter)");
                System.out.println("(Attention toute progression sera perdu)");
                do{
                    System.out.println("Joueur "+courantPlayer+" a votre tour :");
                    coulSelect = sc.nextLine().toUpperCase().charAt(0);
                }while(!choixCouleur(coulSelect, courantPlayer) );
            }
                
            switch (courantPlayer){
                 case 1:
                     parcourirGrille(J1.couleur, coulSelect);
                     //nouvelle couleur du joueur
                     J1.couleur = coulSelect;
                     couleursPlayer[0] = coulSelect;
                     
                     //on verifie si le joueur 1 a gagné
                     if(board.isFinish(couleursPlayer[0]))
                         partieTerminee = true;
                     break;
                 case 2:
                     parcourirGrille(J2.couleur, coulSelect);
                     J2.couleur = coulSelect;
                     couleursPlayer[1] = coulSelect;
                     
                     if(board.isFinish(couleursPlayer[1]))
                         partieTerminee = true;
                     break;
                 case 3:
                     parcourirGrille(J3.couleur, coulSelect);
                     J3.couleur = coulSelect;
                     couleursPlayer[2] = coulSelect;
                     
                     if(board.isFinish(couleursPlayer[2]))
                         partieTerminee = true;
                     break;
                 case 4:
                     parcourirGrille(J4.couleur, coulSelect);
                     J4.couleur = coulSelect;
                     couleursPlayer[3] = coulSelect;
                     
                     if(board.isFinish(couleursPlayer[3]))
                         partieTerminee = true;
                     break;
            }
        }
        
        System.out.println(quiEstGagnant());
        System.out.println("Félicitations !!");
        board.afficher();
        
    }

//=============================================================
    
    public static String quiEstGagnant(){
        int[] points = {0,0,0,0};
        
        //on compte le nombre de points de chaque joueur
        for (int i=0; i< tailleGrille; i++){
            for (int j=0; j< tailleGrille; j++){
                for (int k=0; k < nombrePlayer; k++){
                    if(board.grille[i][j] == couleursPlayer[k])
                        points[k]++;
                }
            }
        }
        //on cherche le maximum du tableau ce qui nous donne
        //le numero du gagnant
        int max = -1;
        int indice_max = -1;
        for (int m=0; m<4; m++){
            if (points[m] > max){
                max = points[m];
                indice_max = m;
            }
        }
        return "Le joueur "+(indice_max+1)+" gagne avec "+max+" points !";
    }
    
    public static void ajoutPlayer(){
        //on ajoute des joueurs et on definit leurs paramètres 
        
        char coul; 
        char[] all = {'R','V','B','I','J','O'};
                
        if (nombrePlayer >=1){
            do{
                coul = all[(int)(Math.random()*6)];
            }while(!choixCouleur(coul, 1));
            couleursPlayer[0] = coul;
            J1 = new Joueurs(1, coul , board);
        }    
        if (nombrePlayer >=2){
            do{
            	 coul = all[(int)(Math.random()*6)];
            }while(!choixCouleur(coul, 2));
            couleursPlayer[1] = coul;
            J2 = new Joueurs(2, coul , board);;
        }
        if (nombrePlayer >=3){
            do{
            	  coul = all[(int)(Math.random()*6)];
            }while(!choixCouleur(coul, 3));
            couleursPlayer[2] = coul;
            J3 = new Joueurs(3, coul , board);
        }
        if (nombrePlayer >=4){
            do{
            	  coul = all[(int)(Math.random()*6)];
            }while(!choixCouleur(coul, 4));
            couleursPlayer[3] = coul;
            J4 = new Joueurs(4, coul , board);
        }
    }
    
    public static boolean choixCouleur(char coul, int player){
        /** renvoie true si la couleur choisit existe et 
         * n'est pas utilisée par les adversaires
         */
        char[] all = {'R','V','B','I','J','O'};

        if(isIn(coul, all) && !isIn(coul, couleursPlayer)){
            return true;
        }
        return false;
    }
    
    public static boolean isIn(char element, char[] liste){
        //fonction qui renvoie true si l'element est dans la liste
        for(int i=0; i<liste.length; i++){
            if (liste[i]==element)
                return true;
        }
        return false;
    }
    
    public static boolean isIn(int element, int[] liste){
        //fonction qui renvoie true si l'element est dans la liste
        for(int i=0; i<liste.length; i++){
            if (liste[i]==element)
                return true;
        }
        return false;
    }
    public static void joueursSuivant(){
        // on incremente le courantPlayer jusqu'au nombre max de joueur
        if ((courantPlayer%Joueurs.getNombrePlayer())==0)
            courantPlayer = 1;
        else
            courantPlayer++;
    }
    
    public static void parcourirGrille(char coulPerso, char coulSelect){
        //deux boucles pour parcourir tout le tableau
        for (int i=0;i < board.len;i++){
             for (int j=0; j< board.len; j++){
                 
                 //si la couleur est celle du joueur
                 if(board.grille[i][j] == coulPerso){
                     //on remplace son ancienne couleur par la nouvelle
                     board.grille[i][j] = coulSelect;
                     
                     //on ajoute les cases autour de cette nouvelle case
                     for(int i1=-1; i1<2; i1++){
                         for(int i2=-1; i2<2; i2++){
                             try{
                                 //si la couleur minuscule correspond a celle en majuscule
                                 if (board.grille[i+i1][j+i2]== (char)((int)(coulSelect)+32))
                                     board.grille[i+i1][j+i2] = coulSelect;
                             }catch(ArrayIndexOutOfBoundsException e){}
                             //la boucle try catch permet de boucler meme sur les bords
                         }
                     }
                 }
             }
         }
    }
}