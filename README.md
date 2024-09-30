# catalog-hackathon
Solution Overview
Parse JSON Input:

The program reads input data from JSON files that contain polynomial roots represented by keys (x) and values (y) encoded in different bases.
Decode Y Values:

Y values are decoded from their respective bases to obtain their actual numerical values.
Calculate Constant Term (c):

Using Lagrange interpolation, the constant term 
𝑐
c of the polynomial is calculated based on the decoded (x, y) points.
Identify Imposter Points:

The program checks if each point lies on the polynomial curve by comparing calculated y-values with the provided y-values, flagging any discrepancies as imposter points.
