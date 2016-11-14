package Akka.akka;



import java.awt.event.MouseWheelEvent;

import Akka.message.*;
import Akka.util.HouseholderQR;
import Akka.util.Matrix;
import Akka.util.MatrixOp;
import akka.actor.UntypedActor;


public class Server extends UntypedActor {
	

	
	int row ,col;
	Matrix  X,H1_s,H2_s,DF0,O_s;
	
	Matrix A_s,S_s,A1_c,A1_s;
	
	Matrix Q1,R1,DHQ,Q_c,R_c;
	
	Matrix R,DR,FR,R1_s,DHR;
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof Init){
			row = ((Init)message).getRow();
			col = ((Init)message).getCol();
			X = MatrixOp.matrixRandomInit(row-1, row-1);
			
			HouseholderQR householderQR = new HouseholderQR(X);
			householderQR.qr();
			householderQR.getQProcess();
			Matrix H = householderQR.getQ();
			
			getSender().tell(new HFromX(H), getSelf());
		}
		
		// get DF0 from client
		if(message instanceof DF){
			DF0 = ((DF)message).getdFMatrix();
			
		}
		//get H1^'(H1_s) and H2^'(H2_s) from client, compute O' (O_s) 
		
		if(message instanceof H1andH2){
			H1_s=((H1andH2) message).getH1();
			H2_s = ((H1andH2) message).getH2();
			//已经验证o_s = D1 *O * F2
			O_s = MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(H1_s, DF0),H2_s);
//			System.out.println("server o");
//			O_s.print();
//			System.out.println();
//			MatrixOp.matrixInverse(O_s).print();
		}
		
		//Securely mask A
		if(message instanceof AandSCC){
			A_s=((AandSCC) message).getA_s();
			S_s=((AandSCC) message).getS_s();
			//验证A1_c = D1 * o *A * S Fs
			A1_c = MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(O_s, A_s),S_s);
//			System.out.println("A1_c");
//			A1_c.print();
			getSender().tell(new A1CS(A1_c), getSelf());
			
		}
		//get A^(A1_s) from client
		if(message instanceof A1CC){
			//验证了A1_s 与客户端的 QR分解结果相同
			A1_s=((A1CC) message).getA1_s();
			
			HouseholderQR qr = new HouseholderQR(A1_s);
			qr.qr();
			qr.getQProcess();
			qr.getRProcess();
			Q1 = qr.getQ();
			R1= qr.getR();
//			System.out.println("server A^ q");
//			Q1.print();
			
			getSender().tell(new Q1andR1CS(Q1, R1), getSelf());
		}
		
		
		if(message instanceof DHQCC){
			DHQ =((DHQCC)message).getDhq();
		}
		if(message instanceof DHRCC){
			DHR =((DHRCC)message).getDhr();
		}
		
		if(message instanceof Q1CC){
			
			Q_c=MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(MatrixOp.matrixInverse(O_s), DHQ),((Q1CC)message).getQ());
//			System.out.println("Q_c");
//			Q_c.print();
			getSender().tell(new QCS(Q_c), getSelf());
		}
		if(message instanceof R1CC){
			
			R_c=MatrixOp.matrixMultimatrix(MatrixOp.matrixMultimatrix(((R1CC)message).getR(),DHR),MatrixOp.matrixInverse(S_s));
			getSender().tell(new RCS(R_c), getSelf());
		}
	}
	
	

}
