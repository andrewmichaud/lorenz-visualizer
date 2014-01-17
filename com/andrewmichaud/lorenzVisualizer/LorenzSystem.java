package com.andrewmichaud.lorenzVisualizer;

// RK4 implementation in java.
// Andrew Michaud
// 29 April 2013


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.math.*;

class LorenzSystem {
    
    // Global variables
    
    
    // We're working with BigDecimals, so dividing by doubles in
    // order to implement RK4 isn't going to work.  So, I define
    // the numbers I need as constants here, though it feels like
    // bad style.
    public static BigDecimal ONE   = BigDecimal.valueOf(1.0);
    public static BigDecimal TWO   = BigDecimal.valueOf(2.0);
    public static BigDecimal THREE = BigDecimal.valueOf(3.0);
    public static BigDecimal SIX   = BigDecimal.valueOf(6.0);
    public static double MAX_X = 100000000;
    public static double MAX_Y = 100000000;
    public static double MAX_Z = 100000000;

    // Useful data members.
    private List<BigDecimal[]> points_;
    private BigDecimal[] initCoords_;
    private BigDecimal timeStep_;
    private int precision_;
    private boolean debug_ = false;

    // Paramters for the Lorenz Equations.
    private BigDecimal sigma_ = BigDecimal.valueOf(10.0);
    private BigDecimal rho_   = BigDecimal.valueOf(28.0);
    private BigDecimal beta_  = BigDecimal.valueOf(8.0 / 3.0);

    // Calculates the derivative of x(t).
    private BigDecimal xPrime(BigDecimal[] values) {
    
        // Pulling out the values for convenience.
        BigDecimal x = values[0];
        BigDecimal y = values[1];
        BigDecimal z = values[2];
        
        if (debug_) {
            // For debugging
            return x;
            //return ONE.divide(x, new MathContext(precision_));

        } else {
            // Formula: x' = sigma_ * (y - x)
            return sigma_.multiply( y.subtract(x) );
        }
    }

    // Calculates the derivative of y(t).
    private BigDecimal yPrime(BigDecimal[] values) {
        
        // Pulling out the values for convenience.
        BigDecimal x = values[0];
        BigDecimal y = values[1];
        BigDecimal z = values[2];
        
        if (debug_) {
            // For debugging.
            return y;
            //return ONE.divide(y, new MathContext(precision_));

        } else {
            // Formula: y' = (x * (rho_ - z)) - y
            // y' = rho x - y + xz
            return ( x.multiply( rho_.subtract(z) ) ).subtract(y);
        } 
    }

    // Calculates the derivative of z(t).
    private BigDecimal zPrime(BigDecimal[] values) {
        
        // Pulling out the values for convenience.
        BigDecimal x = values[0];
        BigDecimal y = values[1];
        BigDecimal z = values[2];

        if (debug_) {
            // For debugging.
            return z;
            //return ONE.divide(z, new MathContext(precision_));

        } else {
            // Formula: z' = (x * y) - (beta_ * z)
            return ( x.multiply(y) ).subtract( beta_.multiply(z));
        } 
    }

    private BigDecimal[] rk4(BigDecimal[] coords) {
        // Declaring these for convenience when calculating formulas.
        BigDecimal h = timeStep_;
        
        BigDecimal x = coords[0];
        
        BigDecimal y = coords[1];
        
        BigDecimal z = coords[2];
        
        BigDecimal hDivTwo = h.divide(TWO, new MathContext(precision_));

        BigDecimal hDivThree = h.divide(THREE, new MathContext(precision_));
        
        BigDecimal hDivSix = h.divide(SIX, new MathContext(precision_));
        

        // Declaring and filling A vector.
        BigDecimal[] aVec = new BigDecimal[3];
        
        // Array of values we use to get new variables.
        // Formula; val_n,j = x_n,j
        BigDecimal[] aValues = {x, y, z};
        
        // Formula: a_n,x = f_x(val_n,t, val_n,x, val_n,y, val_n,z)
        aVec[0] = xPrime(aValues);
        
        // Formula: a_n,y = f_y(val_n,t, val_n,x, val_n,y, val_n,z)
        aVec[1] = yPrime(aValues);

        // Formula: a_n,z = f_z(val_n,t, val_n,x, val_n,y, val_n,z)
        aVec[2] = zPrime(aValues);


        // Declaring and filling B vector.
        BigDecimal[] bVec = new BigDecimal[3];
        
        // Array of values we use to get new variables.
        // Formula; val_n,j = x_n,j + (h / 2) * a_n,j
        BigDecimal[] bValues = {
                                x.add( (hDivTwo).multiply(aVec[0]),
                                new MathContext(precision_) ),
                                y.add( (hDivTwo).multiply(aVec[1]),
                                new MathContext(precision_) ),
                                z.add( (hDivTwo).multiply(aVec[2]),
                                new MathContext(precision_) )};
        
        // Formula: b_n,x = f_x(val_n,t, val_n,x, val_n,y, val_n,z)
        bVec[0] = xPrime(bValues);
 
        // Formula: b_n,y = f_y(val_n,t, val_n,x, val_n,y, val_n,z)
        bVec[1] = yPrime(bValues);
        
        // Formula: b_n,z = f_z(val_n,t, val_n,x, val_n,y, val_n,z)
        bVec[2] = zPrime(bValues);


        // Declaring and filling C vector.
        BigDecimal[] cVec = new BigDecimal[3];
        
        // Array of values we use to get new variables.
        // Formula; val_n,j = x_n,j + (h / 2) * b_n,j
        BigDecimal[] cValues = { 
                                x.add( (hDivTwo).multiply(bVec[0]),
                                new MathContext(precision_) ),
                                y.add( (hDivTwo).multiply(bVec[1]),
                                new MathContext(precision_) ),
                                z.add( (hDivTwo).multiply(bVec[2]),
                                new MathContext(precision_) )};

        // Formula: c_n,x = f_x(val_n,t, val_n,x, val_n,y, val_n,z)
        cVec[0] = xPrime(cValues);
        
        // Formula: c_n,y = f_y(val_n,t, val_n,x, val_n,y, val_n,z)
        cVec[1] = yPrime(cValues);
        
        // Formula: c_n,z = f_z(val_n,t, val_n,x, val_n,y, val_n,z)
        cVec[2] = zPrime(cValues);


        // Declaring and filling D vector.
        BigDecimal[] dVec = new BigDecimal[3];

        // Array of values we use to get new variables.
        // Formula; val_n,j = x_n,j + h * c_n,j
        BigDecimal[] dValues = {
                                x.add( h.multiply(cVec[0]),
                                new MathContext(precision_) ),
                                y.add( h.multiply(cVec[1]),
                                new MathContext(precision_) ),
                                z.add( h.multiply(cVec[2]),
                                new MathContext(precision_) )};

        // Formula: d_n,x = f_x(val_n,t, val_n,x, val_n,y, val_n,z)
        dVec[0] = xPrime(dValues);
        
        // Formula: d_n,y = f_y(val_n,t, val_n,x, val_n,y, val_n,z)
        dVec[1] = yPrime(dValues);
        
        // Formula: d_n,z = f_z(val_n,t, val_n,x, val_n,y, val_n,z)
        dVec[2] = zPrime(dValues);


        // Creating final array.
        BigDecimal[] newVec = new BigDecimal[3];
        
        // Formula: x_n+1 = x + ((h / 6) * a_x) +
        //                      ((h / 3) * b_x) + 
        //                      ((h / 3) * c_x) + 
        //                      ((h / 6) * d_x)
        BigDecimal newX = x.add( ( (hDivSix  ).multiply(aVec[0]) ).add(
                           ( (hDivThree).multiply(bVec[0]) ).add(
                           ( (hDivThree).multiply(cVec[0]) ).add(
                           ( (hDivSix  ).multiply(dVec[0]) ), 
                           new MathContext(precision_) ),
                           new MathContext(precision_)),
                           new MathContext(precision_)),
                           new MathContext(Math.max(10, precision_)));
        
        // Converting to doubles for comparision purposes.
        double newXMag = Math.abs(newX.doubleValue());

        // Comparing.  We limit our points to under 10000 in all directions.
        newVec[0] = BigDecimal.valueOf(Math.min(newXMag, MAX_X));

        // Formula: y_n+1 = y + ((h / 6) * a_y) +
        //                      ((h / 3) * b_y) + 
        //                      ((h / 3) * c_y) + 
        //                      ((h / 6) * d_y)
        BigDecimal newY = y.add( ( (hDivSix  ).multiply(aVec[1]) ).add(
                           ( (hDivThree).multiply(bVec[1]) ).add(
                           ( (hDivThree).multiply(cVec[1]) ).add(
                           ( (hDivSix  ).multiply(dVec[1]) ),
                           new MathContext(precision_) ),
                           new MathContext(precision_)),
                           new MathContext(precision_)),
                           new MathContext(Math.max(10, precision_)));

        // Converting to doubles for comparision purposes.
        double newYMag = Math.abs(newY.doubleValue());

        // Comparing.  We limit our points to under 10000 in all directions.
        newVec[1] = BigDecimal.valueOf(Math.min(newYMag, MAX_Y));


        // Formula: z_n+1 = z + ((h / 6) * a_z) +
        //                      ((h / 3) * b_z) + 
        //                      ((h / 3) * c_z) + 
        //                      ((h / 6) * d_z)
        BigDecimal newZ = z.add( ( (hDivSix  ).multiply(aVec[2]) ).add(
                           ( (hDivThree).multiply(bVec[2]) ).add(
                           ( (hDivThree).multiply(cVec[2]) ).add(
                           ( (hDivSix  ).multiply(dVec[2]) ),
                           new MathContext(precision_) ),
                           new MathContext(precision_)),
                           new MathContext(precision_)),
                           new MathContext(Math.max(10, precision_)));

        // Converting to doubles for comparision purposes.
        double newZMag = Math.abs(newZ.doubleValue());

        // Comparing.  We limit our points to under 10000 in all directions.
        newVec[2] = BigDecimal.valueOf(Math.min(newZMag, MAX_Z));
        
        return newVec;
    }

    public void rk4Range(double initTime, double finalTime, double timeStep) {

        // Creating a variable to modify.
        BigDecimal[] coords = this.initCoords_;

        // Calculating precision we're using on the timeStep.
        String timeStepString = Double.toString(timeStep);
        int posOfDecimal = timeStepString.indexOf(".");
        precision_ = 1;
        if (posOfDecimal > 0) {
            precision_ = timeStepString.length() - posOfDecimal - 1;
        }

        // Saving the timestep.
        timeStep_ = BigDecimal.valueOf(timeStep);

        // Creating begin and end to use for iteration.
        BigDecimal begin = BigDecimal.valueOf(initTime);
        BigDecimal end = BigDecimal.valueOf(finalTime);

        // Repeating the RK4 algorithm until we reach finalTime.
        for (BigDecimal i = begin; 
             i.compareTo(end) < 0; 
             i = i.add(timeStep_)) { 

            coords = rk4(coords);
            points_.add(coords);
        }

    }


    public LorenzSystem(Double[] initCoords, Double[] params) {
        initCoords_ = new BigDecimal[3];
        for (int i = 0; i < initCoords.length; i++) {
            initCoords_[i] = BigDecimal.valueOf(initCoords[i]);
        }
        rho_ = BigDecimal.valueOf(params[0]);
        sigma_ = BigDecimal.valueOf(params[1]);
        //beta_ = BigDecimal.valueOf(params[2]);
        points_ = new ArrayList<BigDecimal[]>();
        points_.add(initCoords_);
    }


    public void print() {
        for (BigDecimal[] point: points_) {
            System.out.print("( ");
            System.out.print(point[0] + ", ");
            System.out.print(point[1] + ", ");
            System.out.print(point[2]);
            System.out.println(" )");
        }
    }


    public Double[] getLastPoint() {
        Double[] lastPoint = new Double[3];
        BigDecimal[] lastArray = points_.get(points_.size() - 1);
        for (int i = 0; i < lastArray.length; i++) {
            lastPoint[i] = lastArray[i].doubleValue();
        }
        return lastPoint;
    }


    static public <T> void printArray(T[] objArray) {
        System.out.print("[ ");
        for (Object obj : objArray) {
            System.out.print(obj + " ");
        }
        System.out.println("]");
    }
    
    
    public List<BigDecimal[]> getPoints() {
        return points_;
    }
    
    public static void main(String[] args) { 
       /*
        // Initial coordinates.
        Double[] initCoords = {0, 1, 1, 1};
        
        // Creating lorenzSystem.
        LorenzSystem ls = new LorenzSystem(initCoords);
        
        if (true) {
            ls.debug_ = true;
        }

        // Running RK4 from 0 to 1.
        ls.rk4Range(initCoords[0], 100, 0.001);
        
        // Printing the results.
        ls.print();
        LorenzSystem.printArray(ls.getLastPoint());
        * 
        */
    }

}
