package linearAlgebra;

public class MatrixSolvLU {
	
	public MatrixSolvLU () {}
	
	public static void ludcmp(int n, double a[][], int indx[]) {
	/*Given a matrix a[1..n][1..n], this routine replaces it by the LU decomposition of a rowwise
	permutation of itself. a and n are input. a is output, arranged as in equation (2.3.14) above;
	indx[1..n] is an output vector that records the row permutation effected by the partial
	pivoting; d is output as ±1 depending on whether the number of row interchanges was even
	or odd, respectively. This routine is used in combination with lubksb to solve linear equations
	or invert a matrix.*/
		
		int i,j,k;
		int imax=n-1;
		double TINY = 1e-20;
		double big,dum,sum;
		double vv[] = new double[n]; 	//vv stores the implicit scaling of each row.

		for (i=0;i<=n-1;i++)		//Loop over rows to get the implicit scaling information.
		{
			big=0.0f;
			for (j=0;j<=n-1;j++)
				if (Math.abs(a[i][j]) > big) big=Math.abs(a[i][j]);
			if (big == 0.0) System.out.println("Singular matrix(determinant is zero) in routine ludcmp");
			//No nonzero largest element.
			vv[i]=1.0f/big; 		//Save the scaling.
		}
		for (j=0;j<=n-1;j++)		//This is the loop over columns of Crout's method.
		{
			for (i=0;i<j;i++)		//This is equation (2.3.12) except for i = j.
			{
				sum=a[i][j];
				for (k=0;k<i;k++) sum -= a[i][k]*a[k][j];
				a[i][j]=sum;
			}
			big=0.0f; 				//Initialize for the search for largest pivot element.
			for (i=j;i<=n-1;i++)	////This is i = j of equation (2.3.12) and i = j+1. . .N of equation (2.3.13).
			{
				sum=a[i][j]; 
				for (k=0;k<j;k++)
					sum -= a[i][k]*a[k][j];
				a[i][j]=sum;
				if (vv[i]*Math.abs(sum) >= big) {
					//Is the figure of merit for the pivot better than the best so far?
					big=vv[i]*Math.abs(sum);
					imax=i;
				}
			}
			if (j != imax)			//Do we need to interchange rows?
			{
				for (k=0;k<=n-1;k++) //Yes, do so...
				{ 
					dum=a[imax][k];
					a[imax][k]=a[j][k];
					a[j][k]=dum;
				}
				vv[imax]=vv[j]; 	//Also interchange the scale factor.
			}
			indx[j]=imax;
			if (a[j][j] == 0.0) a[j][j]=TINY;
			/*If the pivot element is zero the matrix is singular (at least to the precision of the
			algorithm). For some applications on singular matrices, it is desirable to substitute
			TINY for zero.*/
			if (j != n-1)				//Now, finally, divide by the pivot element.
			{
				dum=1.0f/(a[j][j]);
				for (i=j+1;i<=n-1;i++) a[i][j] *= dum;
			}
		} 							//Go back for the next column in the reduction.
	}

	
	
	public static void lubksb(int n, double a[][], int indx[], double b[]) {
	/*Solves the set of n linear equations A dot X = B. Here a[1..n][1..n] is input, not as the matrix
	A but rather as its LU decomposition, determined by the routine ludcmp. indx[1..n] is input
	as the permutation vector returned by ludcmp. b[1..n] is input as the right-hand side vector
	B, and returns with the solution vector X. a, n, and indx are not modified by this routine
	and can be left in place for successive calls with different right-hand sides b. This routine takes
	into account the possibility that b will begin with many zero elements, so it is efficient for use
	in matrix inversion.*/
		int i,ii,ip,j;
		double sum;
		ii=-1;
		for (i=0;i<=n-1;i++) 
		{
			/*When ii is set to a positive value, it will become the
			index of the first nonvanishing element of b. Wenow
			do the forward substitution, equation (2.3.6). The
			only new wrinkle is to unscramble the permutation
			as we go.*/
			ip=indx[i];
			sum=b[ip];
			b[ip]=b[i];
			if (ii != -1)
				for (j=ii;j<=i-1;j++) sum -= a[i][j]*b[j];
			else if (sum != 0.0f) ii=i; 
			/*A nonzero element was encountered, so from now on we
			will have to do the sums in the loop above.*/
			b[i]=sum;
		}
		for (i=n-1;i>=0;i--)	//Now we do the backsubstitution, equation (2.3.7). 
		{
			sum=b[i];
			for (j=i+1;j<=n-1;j++) sum -= a[i][j]*b[j];
			b[i]=sum/a[i][i]; //Store a component of the solution vector X.
		} //All done!
	}

	public static Vector3[] lubksb(int n, double a[][], int indx[], Vector3 b[]) {
		Vector3 v[] = new Vector3[n];
		double x[] = new double[n];
		double y[] = new double[n];
		double z[] = new double[n];
		for (int i = 0; i < n; i++) {
			x[i] = b[i].getX();
			y[i] = b[i].getY();
			z[i] = b[i].getZ();
		}
		lubksb(n, a, indx, x);
		lubksb(n, a, indx, y);
		lubksb(n, a, indx, z);
		for (int i = 0; i < n; i++) {
			v[i] = new Vector3(x[i], y[i], z[i]);
		}
		return v;
	}
}
