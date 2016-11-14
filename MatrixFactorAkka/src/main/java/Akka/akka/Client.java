package Akka.akka;


import java.lang.ref.ReferenceQueue;

import Akka.message.*;
import Akka.util.HouseholderQR;
import Akka.util.Matrix;
import Akka.util.MatrixOp;
import Akka.util.Vector;
import Akka.util.VectorOp;
import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Deploy;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.remote.RemoteScope;
import akka.routing.SeveralRoutees;

public class Client extends UntypedActor{
	int row, col;
	Vector X;
	Matrix O,D1,D2,F1,F2,H1,DF0;
	
	Matrix A,S,A_s,S_s,A1_s;
	Matrix Da,Fa,Ds,Fs;
	
	Matrix Q,Q1,DQ,FQ,Q1_s,DHQ_s;
	
	Matrix R,R1,DR,FR,R1_s,DHR_s,qtest;
	ActorRef server;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof Init){
			//init
			row = ((Init)message).getRow();
			col = ((Init)message).getCol();
			
//			Address address1 = new Address("akka.tcp", "RemoteSys", "192.168.18.60", 2558);
//			server = getContext().actorOf(Props.create(Server.class).withDeploy(new Deploy(new RemoteScope(
//					address1))));
			
			
			server = getContext().actorOf(Props.create(Server.class),"woker1");
			server.tell(new Init(row, col), getSelf());
			X = VectorOp.randomInit(row);
			D1 = MatrixOp.diagInit(row, row);
			D2 = MatrixOp.diagInit(row, row);
			F1 = MatrixOp.diagInit(row, row);
			F2 = MatrixOp.diagInit(row, row);
			S = MatrixOp.upperTrianRandomInit(col, col);
			Da = MatrixOp.diagInit(row, row);
			Ds = MatrixOp.diagInit(col, col);
			Fa = MatrixOp.diagInit(col, col);
			Fs = MatrixOp.diagInit(col, col);
			
			DQ = MatrixOp.diagInit(row, row);
			FQ = MatrixOp.diagInit(row, row);
			DR = MatrixOp.diagInit(col, col);
			FR = MatrixOp.diagInit(col, col);
			A = new Matrix(row, col);
			MatrixOp.matrixInit(A);
			//get H1
			H1 =VectorOp.outerProduct(X, X);
			MatrixOp.ident_Sub(H1, 2, VectorOp.norm(X));
			
			 //get DF0		
			DF0 = MatrixOp.diagMultidiag(MatrixOp.diagonalMatrixInverse(F1),MatrixOp.diagonalMatrixInverse(D2));
			
			server.tell(new DF(DF0), getSelf());
		}
		if(message instanceof HFromX){
			//create H1^'=H1_s  H2^'=H2_s, H1'=H1,H2'=H2
			Matrix H2 = MatrixOp.extendMatrix(((HFromX)message).getH(),1);
			
			O = MatrixOp.matrixMultimatrix(H1, H2);
//			System.out.println("O");
//			O.print();
//			O.print();
//			
//			System.out.println(MatrixOp.innerProduct(O, O, 1,1));
			
			Matrix H1_s=MatrixOp.matrixMultiDiag( MatrixOp.diagMultiMatrix(D1, H1),F1);
			Matrix H2_s=MatrixOp.matrixMultiDiag( MatrixOp.diagMultiMatrix(D2, H2),F2);
//			System.out.println("client O' ");
//			MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(H1_s, DF0),H2_s).print();;
//			System.out.println();
//			MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(D1, O),F2).print();;
			
			
			server.tell(new H1andH2(H1_s, H2_s), getSelf());
			
			//Securely mask A begin
			//generate A
			Fa = MatrixOp.diagonalMatrixInverse(F2);
			Ds = MatrixOp.diagonalMatrixInverse(Da);
			A_s = MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(Fa, A), Da);
			S_s= MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(Ds, S), Fs);
			
//			Matrix oas =MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(O, A),S);
//			System.out.println("client A^'");
//			MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(D1,oas),Fs).print();;
			
			server.tell(new AandSCC(A_s, S_s), getSelf());
			
			
			
		}
		//A^ = A1_s
		if(message instanceof A1CS){
			
			A1_s =MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(MatrixOp.diagonalMatrixInverse(D1) ,((A1CS) message).getA1_c())
					, MatrixOp.diagonalMatrixInverse(Fs));
			O.print();
			System.out.println();
			A.print();
			System.out.println();
			S.print();
			HouseholderQR qr = new HouseholderQR(A1_s);
			qr.qr();
			qr.getQProcess();
			qr.getRProcess();		
			System.out.println();
			System.out.println("client A^ q");
			qr.getQ().print();
			System.out.println();
			MatrixOp.matrixMultimatrix(MatrixOp.matrixInverse(O), qr.getQ()).print();
			System.out.println();		
			
			MatrixOp.matrixMultimatrix(qr.getR(),MatrixOp.matrixInverse(S) ).print();
			System.out.println("A");
			qr = new HouseholderQR(A);
			qr.qr();
			qr.getQProcess();
			qr.getRProcess();		
			System.out.println();
			qr.getQ().print();;
			System.out.println();		
			System.out.println();
			qr.getR().print();
//			MatrixOp.matrixMultimatrix(MatrixOp.matrixInverse(O),MatrixOp.matrixMultimatrix(A1_s, MatrixOp.matrixInverse(S))).print();
//			A.print();
			//securely decompose the matrix A^
			server.tell( new A1CC(A1_s), getSelf());
			
			//securely recover
			DHQ_s=MatrixOp.diagMultidiag(D1, MatrixOp.diagonalMatrixInverse(FQ));
			
//			System.out.println("FQ");
//			FQ.print();
			
			server.tell(new DHQCC(DHQ_s), getSelf());
			
			DHR_s=MatrixOp.diagMultidiag( MatrixOp.diagonalMatrixInverse(DR),Fs);
			server.tell(new DHRCC(DHR_s), getSelf());
		}
		//get Q^(Q1) R^(R1) from server
		if(message instanceof Q1andR1CS){
			Q1=((Q1andR1CS)message).getQ1();
			
			Q1_s =MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(FQ, Q1),DQ);
//			System.out.println("client");
//			Matrix oMatrix=MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(D1, O),F2);
//			MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(MatrixOp.matrixInverse(oMatrix), ),Q1_s).print();;
			server.tell(new Q1CC(Q1_s), getSelf());
			
			R1=((Q1andR1CS)message).getR1();
			
			R1_s =MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(FR, R1),DR);
			server.tell(new R1CC(R1_s), getSelf());
		}
		
		
		if(message instanceof QCS){
			
			Q = MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(F2, ((QCS)message).getQ()),MatrixOp.diagonalMatrixInverse(DQ));
		}
		if(message instanceof RCS){
			
			R = MatrixOp.matrixMultiDiag(MatrixOp.diagMultiMatrix(MatrixOp.diagonalMatrixInverse(FR), ((RCS)message).getR()),Ds);
			HouseholderQR qr = new HouseholderQR(A);
			qr.qr();
			qr.getQProcess();
			qr.getRProcess();
			Q.print();
			System.out.println();
			qr.getQ().print();
			System.out.println();
			R.print();
			System.out.println();
			qr.getR().print();
			context().stop(server);
		}
	}
	
	
	

}
