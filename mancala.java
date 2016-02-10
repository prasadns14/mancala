import java.io.*;
import java.util.*;

public class mancala {
	
	static int count = 0;
	static int myId = 0;
	static int cutOffDepth = 0;
	static int val = -999999;
	final static String[][] cellName = {{"root","A2","A3","A4","A5","A6","A7","A8","A9","A10","A11","root"},{"root","B2","B3","B4","B5","B6","B7","B8","B9","B10","B11","root"}};
	static int[][] nextState = new int[2][12];
	
	public static int[][] arrayDup(int[][] a){
		int aDup[][] = new int[2][count+2];
		for(int i = 0; i < 2; i++){
			for(int j = 0; j <= count+1; j++){
				aDup[i][j] = a[i][j];
			}
		}
		return aDup;
	}
	
	public static int max(int a, int b){
		if(a > b){
			return a;
		}else{
			return b;
		}
	}
	
	public static int min(int a, int b){
		if(a < b){
			return a;
		}else{
			return b;
		}
	}
	
	public static int eval(int[][] game){
		if(myId == 2){
			return(game[0][0] - game[1][count+1]);
		}else{
			return(game[1][count+1] - game[0][0]);
		}
	}
	
	public static int[][] maxalphaBeta(int[][] game, int depth, int i, int j, int alpha1, int beta1, PrintWriter w){
		int[][] act = new int[2][count+2];
		act = arrayDup(game);
		int num = act[i][j];
		act[i][j]=0;
		int idx = i;
		int idy = j;
		int alpha = alpha1;
		int beta = beta1;
		
		//Play the pit
		for(int n = 1; n <= num; n++){
			if(i == 1){
				j++;
			}else{
				j--;
			}
			act[i][j]++;
			
			//Changeover 
			if((num-n) >= 1){
				if(i == idx){
					if(j == count+1){
						i = 0;
					}
					else if(j == 0){
						i = 1;
					}
				}else{
					if(i == 0){
						if(j == 1){
							j = 0;
							i = 1;
						}
					}
					if(i == 1){
						if(j == count){
							j = count + 1;
							i = 0;
						}
					}
				}
			}
		}
		if(i == idx){
			if((j==count+1) || (j == 0)){	
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
					return act;
				}else{
					int V = 999999;
					int ans = V;
					int[][] recur = arrayDup(act);
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
					for(int m = 1; m <= count; m++){
						if(act[idx][m] != 0){		
							int[][] ifNext = maxalphaBeta(act, depth, idx, m, alpha, beta, w);
							V = min(V, eval(ifNext));
							if(ans > V){
								ans = V;
								recur = arrayDup(ifNext);
							}
							if(V <= alpha){
								w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
								return recur;
							}
							beta = min(V, beta);
							w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
						}
					}
					return recur;
				}
			}
			else if(act[i][j] == 1){
				int gain = act[i][j] + act[(i+1)%2][j];
				act[i][j] = 0;
				act[(i+1)%2][j] = 0;
				if(i==1){
					act[1][count+1] += gain;
				}else{
					act[0][0] += gain;
				}
				if(endState(act, idx)){	
					int sum = 0;
					int p = (idx+1)%2;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
					return act;
				}else if(endState(act, (idx+1)%2)){
					int sum = 0;
					int p = idx;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
					return act;
				}
			}		
		}else{
			if(endState(act, idx)){
				int sum = 0;
				int p = (idx+1)%2;
				for(int x = 1; x <= count; x++){
					sum += act[p][x];
					act[p][x] = 0;
				}
				if(p==0){
					act[p][0] += sum;
				}else{
					act[p][count+1] += sum;
				}
				w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
				return act;
			}
			if(endState(act, (idx+1)%2)){
				int sum = 0;
				int p = idx;
				for(int x =1; x <= count; x++){
					sum += act[p][x];
					act[p][x] = 0;
				}
				if(p==0){
					act[p][0] += sum;
				}else{
					act[p][count+1] += sum;
				}
				w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
				return act;
			}
		}
		if(depth < cutOffDepth){
			int[][] recur = new int[2][count+2];
			recur = arrayDup(act);
			int maxV = -999999;
			int ans = maxV;
			w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
			for(int m = 1; m <= count; m++){
				if(act[(idx+1)%2][m] != 0){
					int[][] ifNext = minalphaBeta(act, depth+1, (idx+1)%2, m, alpha, beta, w);
					maxV = max(maxV, eval(ifNext));
					if(ans < maxV){
						ans = maxV;
						recur = arrayDup(ifNext);
					}
					if(maxV >= beta){
						w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
						return recur;
					}
					alpha = max(alpha, maxV);
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
				}		
			}
			return recur;
		}
		w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
		return act;
	}
	
	public static int[][] minalphaBeta(int[][] game, int depth, int i, int j, int alpha1, int beta1, PrintWriter w){
		int num = game[i][j];
		int[][] act = new int[2][count+2];
		act = arrayDup(game);
		act[i][j]=0;
		int idx = i;
		int idy = j;
		int alpha = alpha1;
		int beta = beta1;
		for(int n = 1; n <= num; n++){
			if(i == 1){
				j++;
			}else{
				j--;
			}
			act[i][j]++;
			if((num-n) >= 1){
				if(i == idx){
					if(j == count+1){
						i = 0;
					}
					else if(j == 0){
						i = 1;
					}
				}else{
					if(i == 0){
						if(j == 1){
							j = 0;
							i = 1;
						}
					}
					if(i == 1){
						if(j == count){
							j = count + 1;
							i = 0;
						}
					}
				}
			}
		}
		if(i == idx){
			if((j == count+1) || (j==0)){
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p == 0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
					if(depth == 1){
						if(eval(act) > val){
							val = eval(act);
							nextState = arrayDup(act);
						}
					}
					return act;
				}else{
					int V = -999999;
					int ans = V;
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
					int[][] recur = arrayDup(act);
					for(int m = 1; m <= count; m++){
						if(act[idx][m] != 0){
							int[][] ifNext = minalphaBeta(act, depth, idx, m, alpha, beta, w);
							V = max(V, eval(ifNext));
							if(ans < V){
								ans = V;
								recur = arrayDup(ifNext);
							}
							if(V >= beta){
								w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
								return recur;
							}
							alpha = max(alpha, V);
							w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
						}
					}
					return recur;
				}
			}else if(act[i][j] == 1){
				int gain = act[i][j] + act[(i+1)%2][j];
				act[i][j] = 0;
				act[(i+1)%2][j] = 0;
				if(i==1){
					act[1][count+1] += gain;
				}else{
					act[0][0] += gain;
				}
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x =1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					if(depth == 1){
						if(eval(act) > val){
							val = eval(act);
							nextState = arrayDup(act);
						}
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
					return act;
				}else if(endState(act, (idx+1)%2)){
					int sum = 0;
					int p = idx;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					if(depth == 1){
						if(eval(act) > val){
							val = eval(act);
							nextState = arrayDup(act);
						}
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
					return act;
				}
			}
		}else{
			if(endState(act, idx)){
				int sum = 0;
				int p = (idx+1)%2;
				for(int x = 1; x <= count; x++){
					sum += act[p][x];
					act[p][x] = 0;
				}
				if(p==0){
					act[p][0] += sum;
				}else{
					act[p][count+1] += sum;
				}
				if(depth == 1){
					if(eval(act) > val){
						val = eval(act);
						nextState = arrayDup(act);
					}
				}
				w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
				return act;
			}
		}
		if(depth < cutOffDepth){
			int[][] recur = new int[2][count+2];
			recur = arrayDup(act);
			int minV = 999999;
			int ans = minV;
			w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
			for(int m = 1; m <= count; m++){
				if(act[(idx+1)%2][m] != 0){
					int[][] ifNext = maxalphaBeta(act, depth+1, (idx+1)%2, m, alpha, beta, w);
					minV = min(minV, eval(ifNext));
					if(ans > minV){
						ans = minV;
						recur = arrayDup(ifNext);
					}
					if(minV <= alpha){
						if(depth == 1){
							if(ans > val){
								val = ans;
								nextState = arrayDup(act);
							}
						}
						w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));	
						return recur;
					}
					beta = min(minV, beta);
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
				}
			}
			if(depth == 1){
				if(ans > val){
					val = ans;
					nextState = arrayDup(act);
				}
			}
			return recur;
		}
		w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act))+","+stringValue(alpha)+","+stringValue(beta));
		return act;
	}
	
	public static void alphaBeta(int game[][], int playerNo, PrintWriter w){
		w.println("Node,Depth,Value,Alpha,Beta");
		int V = -999999;
		int ans = V;
		int alpha = -999999;
		int beta = 999999;
		w.println("root"+","+"0"+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
		if(myId0==2){
			for(int i = 1; i <= count; i++){
				if(game[0][i] != 0){
					int[][] ifNext = minalphaBeta(game, 1, 0, i, alpha, beta, w);
					V = max(V, eval(ifNext));
					if(ans < V){
						ans = V;
					}
					if(V >= beta){
						return;
					}
					alpha = max(alpha, V);
					w.println("root"+","+"0"+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
				}
			}
		}else{
			for(int i = 1; i <= count; i++){
				if(game[1][i] != 0){
					int[][] ifNext = minalphaBeta(game, 1, 1, i, alpha, beta, w);
					V = max(V, eval(ifNext));			
					if(ans < V){
						ans = V;
					}
					if(V >= beta){
						return;
					}
					alpha = max(alpha, V);
					w.println("root"+","+"0"+","+stringValue(ans)+","+stringValue(alpha)+","+stringValue(beta));
				}
			}
		}
		return;
	}
	
	public static int[][] maxAction(int[][] game, int depth, int i, int j, PrintWriter w){
		int[][] act = new int[2][count+2];
		act = arrayDup(game);
		int num = act[i][j];
		act[i][j]=0;
		int idx = i;
		int idy = j;
		
		//Play the pit
		for(int n = 1; n <= num; n++){
			if(i == 1){
				j++;
			}else{
				j--;
			}
			act[i][j]++;
			
			//Changeover 
			if((num-n) >= 1){
				if(i == idx){
					if(j == count+1){
						i = 0;
					}
					else if(j == 0){
						i = 1;
					}
				}else{
					if(i == 0){
						if(j == 1){
							j = 0;
							i =1;
						}
					}
					if(i == 1){
						if(j == count){
							j = count + 1;
							i = 0;
						}
					}
				}
			}
		}
		
		if(i == idx){
			if((j==count+1) || (j==0)){	
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x =1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
					return act;
				}else{
					int V = 999999;
					int ans = V;
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
					int[][] recur = arrayDup(act);
					for(int m = 1; m <= count; m++){
						if(act[idx][m] != 0){
							int[][] ifNext = arrayDup(maxAction(act, depth, idx, m, w));
							V = min(V, eval(ifNext));
							if(ans > V){
								ans = V;
								recur = ifNext;
							}
							w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
						}
					}
					return recur;	
				}
			}
			else if(act[i][j] == 1){
				int gain = act[i][j] + act[(i+1)%2][j];
				act[i][j] = 0;
				act[(i+1)%2][j] = 0;
				if(i==1){
					act[1][count+1] += gain;
				}else{
					act[0][0] += gain;
				}
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x =1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
					return act;
				}else if(endState(act, (idx+1)%2)){
					int sum = 0;
					int p = idx;
					for(int x =1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
					return act;
				}
			}
		}else if(endState(act, idx)){
			int sum = 0;
			int p = (idx+1)%2;
			for(int x =1; x <= count; x++){
				sum += act[p][x];
				act[p][x] = 0;
			}
			if(p==0){
				act[p][0] += sum;
			}else{
				act[p][count+1] += sum;
			}
			w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
			return act;
		}
		if(depth < cutOffDepth){
			int[][] recur = new int[2][count+2];
			recur = arrayDup(act);
			int maxV = -999999;
			int ans = maxV;
			w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
			for(int m = 1; m <= count; m++){
				if(act[(idx+1)%2][m] != 0){
					int[][] ifNext = arrayDup(minAction(act, depth+1, (idx+1)%2, m, w));
					maxV = max(maxV, eval(ifNext));
					if(ans < maxV){
						ans = maxV;
						recur = ifNext;	
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
				}		
			}
			return recur;
		}
		w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
		return act;
	}
		
	public static int[][] minAction(int[][] game, int depth, int i, int j, PrintWriter w){
		int[][] act = new int[2][count+2];
		act = arrayDup(game);
		int num = act[i][j];
		act[i][j]=0;
		int idx = i;
		int idy = j;
		for(int n = 1; n <= num; n++){
			if(i == 1){
				j++;
			}else{
				j--;
			}
			act[i][j]++;
			if((num-n) >= 1){
				if(i == idx){
					if(j == count+1){
						i = 0;
					}
					else if(j == 0){
						i = 1;
					}
				}else{
					if(i == 0){
						if(j == 1){
							j = 0;
							i =1;
						}
					}
					if(i == 1){
						if(j == count){
							j = count + 1;
							i = 0;
						}
					}
				}
			}
		}
	
		if(i == idx){
			if((j == count+1) || (j == 0)){
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p == 0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					if(depth == 1){
						if(eval(act) > val){
							val = eval(act);
							nextState = arrayDup(act);
						}
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
					return act;
				}else{
					int V = -999999;
					int ans = V;
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
					int[][] recur = arrayDup(act);
					for(int m = 1; m <= count; m++){
						if(act[idx][m] != 0){
							int[][] ifNext = arrayDup(minAction(act, depth, idx, m, w));
							V = max(V, eval(ifNext));
							if(ans < V){
								ans = V;
								recur = ifNext;
							}
							w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
						}
					}
					return recur;
			    }
			}
			else if(act[i][j] == 1){
				int gain = act[i][j] + act[(i+1)%2][j];
				act[i][j] = 0;
				act[(i+1)%2][j] = 0;
				if(i==1){
					act[1][count+1] += gain;
				}else{
					act[0][0] += gain;
				}
				if(endState(act, idx)){
					int sum = 0;
					int p = (idx+1)%2;
					for(int x =1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					if(depth == 1){
						if(eval(act) > val){
							val = eval(act);
							nextState = arrayDup(act);
						}
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
					return act;
				}else if(endState(act, (idx+1)%2)){
					int sum = 0;
					int p = idx;
					for(int x =1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p==0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
					if(depth == 1){
						if(eval(act) > val){
							val = eval(act);
							nextState = arrayDup(act);
						}
					}
					return act;
				}
			}
		}else if(endState(act, idx)){
			int sum = 0;
			int p = (idx+1)%2;
			for(int x =1; x <= count; x++){
				sum += act[p][x];
				act[p][x] = 0;
			}
			if(p==0){
				act[p][0] += sum;
			}else{
				act[p][count+1] += sum;
			}
			if(depth == 1){
				if(eval(act) > val){
					val = eval(act);
					nextState = arrayDup(act);
				}
			}
			w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
			return act;
		}
		if(depth < cutOffDepth){
			int[][] recur = new int[2][count+2];
			recur = arrayDup(act);
			int minV = 999999;
			int ans = minV;
			w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
			for(int m = 1; m <= count; m++){
				if(act[(idx+1)%2][m] != 0){
					int[][] ifNext = arrayDup(maxAction(act, depth+1, (idx+1)%2, m, w));
					minV = min(minV, eval(ifNext));
					if(ans > minV){
						ans = minV;
						recur = ifNext;
					}
					w.println(cellName[idx][idy]+","+depth+","+stringValue(ans));
				}
			}
			if(depth == 1){
				if(ans > val){
					val = ans;
					nextState = arrayDup(act);
				}
			}
			return recur;
		}
		if(depth == 1){
			if(eval(act) > val){
				val = eval(act);
				nextState = arrayDup(act);
			}
		}
		w.println(cellName[idx][idy]+","+depth+","+stringValue(eval(act)));
		return act;
	}
	
	public static String stringValue(int n){
		if(n == -999999){ 
			return "-Infinity";
		}else if(n == 999999){
			return "Infinity";
		}else{
			return Integer.toString(n);
		}
	}
	
	public static void minmax(int game[][], int playerNo, PrintWriter w){
		w.println("Node,Depth,Value");
		int V = -999999;
		int ans = V;
		w.println("root"+","+"0"+","+stringValue(ans));
		if(myId==2){
			for(int i = 1; i <= count; i++){
				if(game[0][i] != 0){
					int[][] ifNext = minAction(game, 1, 0, i, w);
					V = max(V, eval(ifNext));
					if(ans < V){
						ans = V;
					}
					w.println("root"+","+"0"+","+stringValue(ans));
				}
			}	
		}else{
			for(int i = 1; i <= count; i++){
				if(game[1][i] != 0){
					int[][] ifNext = minAction(game, 1, 1, i, w);
					V = max(V, eval(ifNext));
					if(ans < V){
						ans = V;
					}
					w.println("root"+","+"0"+","+stringValue(ans));
				}
			}
		}
		return;
	}
	
	public static boolean endState(int[][] a, int id){
		for(int i = 1; i <=count ; i++){
			if(a[id][i] != 0){
				return false;
			}	
		}
		return true;
	}
	
	public static int[][] action(int game[][], int i, int j){
		int[][] act = new int[2][count+2];
		act = arrayDup(game);
		int num = act[i][j];
		act[i][j]=0;
		int id = i;
		for(int n = 1; n <= num; n++){
			if(i==1){
				j++;
			}else{
				j--;
			}
			act[i][j]++;
			if((num-n) >= 1){
				if(i == id){
					if(j == count+1){
						i = 0;
					}
					else if(j == 0){
						i = 1;
					}
				}else{
					if(i == 0){
						if(j == 1){
							j = 0;
							i = 1;
						}
					}
					if(i == 1){
						if(j == count){
							j = count + 1;
							i = 0;
						}
					}
				}
			}
		}
		if(i == id){
			if((j == count+1) || (j==0)){
				if(endState(act, id)){
					int sum = 0;
					int p = (id+1)%2;
					for(int x = 1; x <= count; x++){
						sum += act[p][x];
						act[p][x] = 0;
					}
					if(p == 0){
						act[p][0] += sum;
					}else{
						act[p][count+1] += sum;
					}
					return act;
				}else{
					int maxV = -999999;
					int ans = maxV;
					int[][] next = arrayDup(act);
					for(int m = 1; m <= count; m++){
						if(act[id][m] != 0){
							int[][] ifNext = action(act,id,m);
							maxV = max(maxV, eval(ifNext));
							if(ans < maxV){
								ans = maxV;
								next = arrayDup(ifNext);
							}
						}
					}
					return next;
				}
			}
			else{
				if(act[i][j] == 1){
					int gain = act[i][j] + act[(i+1)%2][j];
					act[i][j] = 0;
					act[(i+1)%2][j] = 0;
					if(i == 1){
						act[1][count+1] += gain;
					}else{
						act[0][0] += gain;
					}
					if(endState(act, id)){
						int sum = 0;
						int p = (id+1)%2;
						for(int x = 1; x <= count; x++){
							sum += act[p][x];
							act[p][x] = 0;
						}
						if(p==0){
							act[p][0] += sum;
						}else{
							act[p][count+1] += sum;
						}
						return act;
					}else if(endState(act, (id+1)%2)){
						int sum = 0;
						int p = id;
						for(int x = 1; x <= count; x++){
							sum += act[p][x];
							act[p][x] = 0;
						}
						if(p == 0){
							act[p][0] += sum;
						}else{
							act[p][count+1] += sum;
						}
						return act;
					}
				}
			}
		}
		if(endState(act, id)){
			int sum = 0;
			int p = (id+1)%2;
			for(int x =1; x <= count; x++){
				sum += act[p][x];
				act[p][x] = 0;
			}
			if(p == 0){
				act[p][0] += sum;
			}else{
				act[p][count+1] += sum;
			}
			return act;
		}
		return act;
	}
	
	public static void greedy(int[][] game, int playerNo){
		int maxV = -999999;
		int[][] next = game;
		int ans = maxV;
		if(playerNo == 1){
			for(int i = 1; i <= count; i++){
				if(game[1][i] != 0){
					int[][] ifNext = action(game,1,i);
					maxV = max(maxV, eval(ifNext));
					if(ans < maxV){
						ans = maxV;
						next = arrayDup(ifNext);
					}
				}
			}
		}else{
			for(int i = 1; i <= count; i++){
				if(game[0][i] != 0){
					int[][] ifNext = action(game,0,i);
					maxV = max(maxV, eval(ifNext));
					if(ans < maxV){
						ans = maxV;
						next = arrayDup(ifNext);
					}
				}
			}
		
		}
		if(eval(next) > val){
			val = eval(next);
			nextState = arrayDup(next);
		}
	}
	
	
	public static void main(String[] args)  {
		try(Scanner sn = new Scanner(new BufferedReader(new FileReader(args[1])))){
			PrintWriter writer = new PrintWriter("next_state.txt", "UTF-8");
			PrintWriter writerLogs = new PrintWriter("traverse_log.txt", "UTF-8");
			int task = Integer.parseInt(sn.nextLine());
			myId = Integer.parseInt(sn.nextLine());
			cutOffDepth = Integer.parseInt(sn.nextLine());
			String p2State = sn.nextLine();
			StringTokenizer strToken = new StringTokenizer(p2State);
			count = strToken.countTokens();
			int[][] game = new int[2][count+2];
			for(int i = 1; i <= count; i++){
				game[0][i] = Integer.parseInt((String)strToken.nextToken());
			}
			String p1State = sn.nextLine();
			strToken = new StringTokenizer(p1State);
			for(int i = 1; i <= count; i++){
				game[1][i] = Integer.parseInt((String)strToken.nextToken());
			}
			game[0][0] = Integer.parseInt(sn.nextLine());
			game[1][count+1] = Integer.parseInt(sn.nextLine());
			if(task == 1){
				greedy(game, myId);
			}else if(task == 2){
				minmax(game, myId, writerLogs);
			}else if(task == 3){
				alphaBeta(game, myId, writerLogs);	
			}
			for(int i = 0; i < 2; i++){
				for(int j = 1; j <= count; j++){
					writer.print(nextState[i][j] + " ");
				}
				writer.println();
			}
			writer.println(nextState[0][0]);
			writer.println(nextState[1][count+1]);
			writer.close();
			writerLogs.close();
		}catch (IOException e){
			System.out.println("File not found");
		}	
	}
}
