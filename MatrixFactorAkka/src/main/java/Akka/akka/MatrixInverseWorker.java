package Akka.akka;

import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;
import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import org.omg.CORBA.PUBLIC_MEMBER;

import Akka.MatrixInverseMessage.Continue;
import Akka.MatrixInverseMessage.Distance;
import Akka.MatrixInverseMessage.DistanceStop;
import Akka.MatrixInverseMessage.GetX;
import Akka.MatrixInverseMessage.Iteration;
import Akka.MatrixInverseMessage.MatrixInverseWorkerInit;
import Akka.MatrixInverseMessage.Message;
import Akka.MatrixInverseMessage.XIteration;
import Akka.MatrixInverseMessage.XNext;
import Akka.MatrixInverseMessage.XResult;
import Akka.util.Matrix;
import Akka.util.MatrixOp;
import Akka.util.Vector;
import Akka.util.VectorOp;
import akka.actor.ActorRef;
import akka.actor.LocalActorRef;
import akka.actor.UntypedActor;
import akka.io.Inet;
import scala.annotation.meta.param;
import scala.collection.generic.BitOperations.Int;
import scala.collection.script.Start;
import scala.languageFeature.reflectiveCalls;
import scala.math.Numeric.IntIsIntegral;

public class MatrixInverseWorker extends UntypedActor{

	
	
	int loc;
	int begin;
	int end;
	int workerNum;
	ArrayList<ActorRef > workerList;
	ActorRef manager;
	double atol;
	Matrix partA;
	Vector[] x;
	Vector[] xNext;
	Vector xNextMes;
	int []seq;
	int xNextSize;
	boolean isInit=false;
	ArrayList<Message> noDealMes = new ArrayList<Message>();
	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if(message instanceof MatrixInverseWorkerInit){
			isInit =true;
			MatrixInverseWorkerInit mes = (MatrixInverseWorkerInit) message;
			this.loc = mes.getLoc();
			this.begin = mes.getBegin();
			this.end = mes.getEnd();
			this.partA = mes.getPartA();
			this.manager = mes.getManager();
			this.workerList = mes.getWorkerList();
			this.workerNum = mes.getWorkerNum();
			this.atol = mes.getAtol();
			
			seq = new int [partA.getCol()];
			System.out.println("[worker "+loc+"]: "+"worker init");
			x= new Vector[partA.getCol()];
			xNext = new Vector[partA.getCol()];
			xNextSize = end-begin;
			System.out.println(xNextSize);
			for(int k=0;k <partA.getCol();k++){
				x[k] = new Vector(partA.getCol());
				xNext[k] = new Vector(end - begin);
			}
			
			for(int k=0;k <partA.getCol();k++){
				computX(x[k],k);
				if(loc ==1&&k==1) xNext[1].print();
				double dis =distance(x[k], xNext[k]);
				
				if( dis <= atol){
					manager.tell(new Distance(dis, k), ActorRef.noSender());
					seq[k] = -1;
				}else{
//					if(k<end&&begin<=k ) System.out.println(xNext[k].getVector()[k-begin]);
					int i=0;
					for(ActorRef ref : workerList){
						if(i!=loc)ref.tell(new XNext(begin, end, k, xNext[k]), ActorRef.noSender());
						i++;
					}
					copyXNextToX(x[k], xNext[k], begin, end);
					xNext[k].reset();
					
					computeXNext(begin, end, k, x[k],true);
				}
			}
			if(noDealMes.size()>0){
				for(int i=0;i<noDealMes.size();i++){
					getSelf().tell(noDealMes.get(i), ActorRef.noSender());
				}
				noDealMes.clear();
			}
			
		}
		
		
		if(message instanceof XNext){
			XNext mes = (XNext) message;
			xNextMes = mes.getxNext();
			
			int partLoc = mes.getPartLoc();
			if(!isInit){
				noDealMes.add(mes);
			}
			else {
//				if(loc == 1)System.out.println("[worker "+loc+"]: "+"get　XNext from "+mes.getPartLoc()+"_ "+mes.getBegin()+" "+mes.getEnd());
				if (seq[partLoc] == -1) {

					manager.tell(new DistanceStop(partLoc), ActorRef.noSender());
					int i = 0;
					for (ActorRef ref : workerList) {
						if (i != loc)
							ref.tell(new XNext(begin, end, partLoc, xNext[partLoc]), ActorRef.noSender());
						i++;
					}
					copyXNextToX(x[partLoc], xNext[partLoc], begin, end);
					xNext[partLoc].reset();

					computeXNext(begin, end, partLoc, x[partLoc], true);
					seq[partLoc] = 0;
				}

				computeXNext(mes.getBegin(), mes.getEnd(), partLoc, xNextMes, false);
				copyXNextToX(x[partLoc], xNextMes,mes.getBegin(), mes.getEnd());
				seq[partLoc]++;
				
				if (seq[partLoc] == workerNum - 1) {
//					if(loc == 1)System.out.println("[worker "+loc+"]: "+"get all XNext from "+partLoc);
//					computX(x[partLoc], partLoc);
					
					computeXNextLastStep(partLoc);
					double dis = distance(x[partLoc], xNext[partLoc]);
//					if(loc == 1&& partLoc ==1){
//					
//							System.out.println("distance ");
//							x[partLoc].print();
//							xNext[partLoc].print();
//							System.out.println("distance end");
//						
//						
//					}
//					if(loc==1&&partLoc ==1){
//						partA.print();
//						x[1].print();
//						xNext[1].print();
//					}
					if (dis <= atol) {
						
//						if(loc ==1){
//							x[1].print();
//						}
						if(loc ==1 &&partLoc==1)System.out.println("[worker "+loc+" ]: "+"dis < atol");
						
						
						manager.tell(new Distance(dis, partLoc), ActorRef.noSender());
//						if(partLoc<end&&begin<=partLoc ) System.out.println(xNext[partLoc].getVector()[partLoc-begin]);
						seq[partLoc] = -1;
					} else {
						int i = 0;
						
						seq[partLoc]=0;
						
						for (ActorRef ref : workerList) {
							if (i != loc)
								ref.tell(new XNext(begin, end, partLoc, xNext[partLoc]), ActorRef.noSender());
							i++;
						}
						copyXNextToX(x[partLoc], xNext[partLoc], begin, end);
						xNext[partLoc].reset();

						computeXNext(begin, end, partLoc, x[partLoc], true);
					}
					
				}
			}
			
		}
		if(message instanceof Continue){
			Continue mes = (Continue) message;
			if(loc==1)System.out.println("continue");
			int partLoc = mes.getPartLoc();
			if (seq[partLoc] == -1) {

				manager.tell(new DistanceStop(partLoc), ActorRef.noSender());
				int i = 0;
				for (ActorRef ref : workerList) {
					if (i != loc)
						ref.tell(new XNext(begin, end, partLoc, xNext[partLoc]), ActorRef.noSender());
					i++;
				}
				copyXNextToX(x[partLoc], xNext[partLoc], begin, end);
				xNext[partLoc].reset();

				computeXNext(begin, end, partLoc, x[partLoc], true);
				seq[partLoc] = 0;
			}
		}
		
		
		if(message instanceof GetX){
			GetX mes = (GetX)message;
			int partLoc = mes.getPartLoc();
			
			System.out.println("worker "+loc+" get X "+partLoc);
			
//			xNext[partLoc].print();
			//if(seq[partLoc]==-1){
				copyXNextToX(x[partLoc], xNext[partLoc], begin, end);
			//}
			manager.tell(new XResult(partLoc, x[partLoc]), ActorRef.noSender());
		}
	}
	/**
	 * 计算xNext
	 * @param begin 开始列位置
	 * @param end   结束列位置
	 * @param partLoc 列位置
	 * @param xMes 其他节点的xNext
	 * @param immediate
	 */
	public void computeXNext(int begin,int  end,int partLoc,Vector xMes,boolean immediate){
		double oldValue=0;
		
		if(immediate){
			for (int i = 0; i < xNextSize; i++) {
				
				double result = 0;
				
				for (int j = begin, k = 0; j < end; j++, k++) {
					
					
					if(j==this.begin+i) continue;
					result += partA.getMatrix()[i][j] * xMes.getVector()[k];
					
				}
				xNext[partLoc].getVector()[i]+=result;
				
				
//				if(diag){
//					xNext[partLoc].getVector()[i] = (1 - result) / 1;
//					partA.getMatrix()[i][i] = oldValue;
//					diag =false;
//				}else {
//					xNext[partLoc].getVector()[i] = (1 - result) / partA.getMatrix()[i][i];
//				}
			}
		}else{
			for(int i =0;i<xNextSize;i++){
				
				double result=0;
				for(int j=begin,k=0;j<end;j++,k++){
					
					if(j==this.begin+i) continue;
					
					result+=partA.getMatrix()[i][j]*xMes.getVector()[k];
				}
				
				xNext[partLoc].getVector()[i]+=result;
				
//				if(diag){
//					xNext[partLoc].getVector()[i] = (1 - result) / 1;
//					partA.getMatrix()[i][i] = oldValue;
//					diag =false;
//				}else {
//					xNext[partLoc].getVector()[i] = (1 - result) / partA.getMatrix()[i][i];
//				} 
			}
		}
//		if(partLoc==1&&loc==1){
//			System.out.println("compute xnext ");
//			xNext[1].print();
//		}
	}
	public void computeXNextLastStep(int partLoc){
		boolean diag=false;
//		if(loc ==1&& partLoc ==1 ) System.out.println("xNext begin");
		for (int i = 0; i < xNextSize; i++) {
			System.out.println(partA.getMatrix()[i][this.begin+i]);
			if(Math.abs(partA.getMatrix()[i][this.begin+i])<1e-10){
				diag =true;
			}
			if(diag){
				if(partLoc == this.begin+i) xNext[partLoc].getVector()[i] = (1 - xNext[partLoc].getVector()[i]+x[partLoc].getVector()[i]) / 1;
				else xNext[partLoc].getVector()[i] = (0 - xNext[partLoc].getVector()[i]+x[partLoc].getVector()[i]) / 1;
				diag =false;
			}else {
				
				if(partLoc == this.begin+i) xNext[partLoc].getVector()[i] = (1 - xNext[partLoc].getVector()[i]) / partA.getMatrix()[i][this.begin+i];
				else 
					xNext[partLoc].getVector()[i] = (0 - xNext[partLoc].getVector()[i]) / partA.getMatrix()[i][this.begin+i];
				
			} 
			
		}
//		if(loc ==1&& partLoc ==1 ) System.out.println("xNext " +xNext[1].getVector()[0]);
	}
	public void copyXNextToX(Vector x, Vector xNext,int start,int end){
		for(int i =start,k=0;i<end;i++,k++){
			x.getVector()[i] = xNext.getVector()[k];
		}
	}

	public void computX(Vector x,int loc ){		
		for(int i =0;i<xNextSize;i++){
			
			double result=0;
			for(int k=0;k<partA.getCol();k++){
				
				if(k==this.begin+i) continue;
				
				result+=partA.getMatrix()[i][k]*x.getVector()[k];
			}
			
			xNext[loc].getVector()[i]+=result;
			

		}
		
		for(int i =begin,k=0 ;i<end;i++,k++){
			if(Math.abs(partA.getMatrix()[k][i] -0) <1e-10)
			{
				if(loc == this.begin + k ) xNext[loc].getVector()[k]=(1- xNext[loc].getVector()[k]+x.getVector()[i])/1;
				else xNext[loc].getVector()[k]=(0- xNext[loc].getVector()[k]+x.getVector()[i])/1;
			}	
			else {
				if(loc == this.begin + k )xNext[loc].getVector()[k]=(1- xNext[loc].getVector()[k])/partA.getMatrix()[k][i];
				else xNext[loc].getVector()[k]=(0- xNext[loc].getVector()[k])/partA.getMatrix()[k][i];
			}
		}
	}
	public double  distance(Vector x,Vector xNext){
		int size = x.getSize();
		double result=0;
		
		for(int i =this.begin,k=0;i<this.end;i++,k++){
			result+= Math.pow(Math.abs((x.getVector()[i]-xNext.getVector()[k])),2);
		}
		return result;
	}
	public static void main(String[] args) {
		
	}

}
