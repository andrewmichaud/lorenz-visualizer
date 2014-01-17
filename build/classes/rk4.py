#! /usr/bin/python

import sys

args = sys.argv

# RK4 test implementation in python.
# Andrew Michaud
# 11 April 2013

# Relevant global variables
SIGMA = 10.0
RHO = 8 / 3.0
BETA = 28.0

def rk4Range(initTime, finalTime, timeStep, valueVector, functionVector):
    curTime = initTime
    while curTime < finalTime:
        print valueVector, ","#, "iteration " + str(curTime)
        valueVector = rk4(valueVector, timeStep, functionVector)
        
        curTime += timeStep

def rk4(valueVector, timeStep, functionVector):
    
    # Using the mathematical variable names to ensure the formula is 
    # correct.
    h = timeStep
    
    # Vector form of RK4 (explicit, I think)
    aVec = []
    for i in xrange(3):
        aVec.append(functionVector[i](valueVector))
    bVec = []
    for i in xrange(3):
        bVec.append(functionVector[i](valueVector) + (h / 2.0) * aVec[i])
    cVec = []
    for i in xrange(3):
        cVec.append(functionVector[i](valueVector) + (h / 2.0) * bVec[i])
    dVec = []
    for i in xrange(3):
        dVec.append(functionVector[i](valueVector) + h * cVec[i])
    newVec = []
    for i in xrange(3):
        newVec.append(valueVector[i] + (h / 6.0) *
                      (aVec[i] + 2 * bVec[i] + 2 * cVec[i] + dVec[i]))
    return newVec
        
def xPrime(valueVector):
    
    # variables
    x = valueVector[0]
    y = valueVector[1]
    z = valueVector[2]
    
    return SIGMA * (y - x)


def yPrime(valueVector):
    
    # variables
    x = valueVector[0]
    y = valueVector[1]
    z = valueVector[2]
    
    return x * (RHO - z) - y

def zPrime(valueVector):
    
    # variables
    x = valueVector[0]
    y = valueVector[1]
    z = valueVector[2]
    
    return (x * y) - (BETA * z)


def lorenzVector():
    
    return [xPrime, yPrime, zPrime]

def simpleX(valueVector):
    return 2 / valueVector[0]

def simpleY(valueVector):
    return 2 / valueVector[1]

def simpleZ(valueVector):
    return 2 / valueVector[2]
    
def simpleVector():
    return [simpleX, simpleY, simpleZ]











