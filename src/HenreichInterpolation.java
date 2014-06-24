// Here is the Java code for my interpolation applet on 
//
//      http://www.wam.umd.edu/~petersd/interp.html
//
//************************************************************************
//  Interp2 Applet by T. von Petersdorff
//
//  Tobias von Petersdorff          
//  Department of Mathematics       
//  University of Maryland          
//  College Park, MD 20742          
//  e-mail:       tvp@math.umd.edu                 
//  WWW:          http://www.wam.umd.edu/~petersd/ 
//  PGP public key available via finger and WWW    
//************************************************************************
//  part of the code stolen from
//          Curve Applet by Michael Heinrichs
// Bug: You can drag points out of the drawing area. But then you have
//      no way of moving them. 

import java.awt.*;
import java.applet.*;

import java.util.Vector;

public class HenreichInterpolation{
  double[] xArray;
  double[] yArray;
  int precision =100;
   public HenreichInterpolation(double[] xArray,double[] yArray){
  this.xArray=xArray;     
  this.yArray=yArray;
   }
    
    private void solveTridiag(double sub[], double diag[], double sup[], double b[], int n){
/*                  solve linear system with tridiagonal n by n matrix a
                    using Gaussian elimination *without* pivoting
                    where   a(i,i-1) = sub[i]  for 2<=i<=n
                            a(i,i)   = diag[i] for 1<=i<=n
                            a(i,i+1) = sup[i]  for 1<=i<=n-1
                    (the values sub[1], sup[n] are ignored)
                    right hand side vector b[1:n] is overwritten with solution 
                    NOTE: 1...n is used in all arrays, 0 is unused */
      int i;
/*                  factorization and forward substitution */
      for(i=2; i<=n; i++){
        sub[i] = sub[i]/diag[i-1];
        diag[i] = diag[i] - sub[i]*sup[i-1];
        b[i] = b[i] - sub[i]*b[i-1];
      }
      b[n] = b[n]/diag[n];
      for(i=n-1;i>=1;i--){
        b[i] = (b[i] - sup[i]*b[i+1])/diag[i];
      }
    }       

    public double interpolate(double xIter) {
	int np = this.xArray.length;           // number of points        double d[] = new double[np];        // Newton form coefficients
        double x[] = xArray;//new double[np];        // x-coordinates of nodes
        double d[] = yArray;
        double y;
        double t;
        double oldy=0;
        double oldt=0;

        int npp = np*precision ;          // number of points used for drawing
	
      
     
        
     
        if (np>1){  
          double a[] = new double[np];
          double t1;
          double t2;
          double h[] = new double[np];
          for (int i=1; i<=np-1; i++){
            h[i] = x[i] - x[i-1];
          }
          if (np>2){
            double sub[] = new double[np-1];
            double diag[] = new double[np-1];
            double sup[] = new double[np-1];
            
            for (int i=1; i<=np-2; i++){
              diag[i] = (h[i] + h[i+1])/3;
              sup[i] = h[i+1]/6;
              sub[i] = h[i]/6;
              a[i] = (d[i+1]-d[i])/h[i+1]-(d[i]-d[i-1])/h[i];
            }
            solveTridiag(sub,diag,sup,a,np-2);
          }
          // note that a[0]=a[np-1]=0
          // draw
          oldt=x[0];
          oldy=d[0];
          for (int i=1; i<=np-1; i++) {   // loop over intervals between nodes
            for (int j=1; j<=precision; j++){
              t1 = (h[i]*j)/precision;
              t2 = h[i] - t1;
              y = ((-a[i-1]/6*(t2+h[i])*t1+d[i-1])*t2 +
                   (-a[i]/6*(t1+h[i])*t2+d[i])*t1)/h[i];
              t=x[i-1]+t1;
              if (xIter==t||Math.abs(xIter-t)<0.01)
                  return y;
              oldt=t;
              oldy=y;
            }
          }
          
        }
        return 1000;
      }
  
    public static void main(String[] args) {
        HenreichInterpolation spline= new HenreichInterpolation(new double[]{0,1,2,3,4},new double[]{0,1,2,1,0});
        System.out.println("The value at  4.0   is " + spline.interpolate(4.0));
        
    }
}


  


//*************************************************************************
// end of code of Interp2 Applet
//*************************************************************************