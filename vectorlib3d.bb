;// Vector Math Library v.05a;

;// Updated Dec 2, 2002
;// Numerous Errors fixed with Cross and Dot Product

;// Updated May 6, 2003
;// New "reflexive" routines added


;// This vector is used for holding calculations
Global Temp.Vector=Vector()
Global tol# = 0.001

Type Vector
	Field x#
	Field y#
	Field z#
End Type


;// Create a Vector
Function Vector.Vector(x#=0.0,y#=0.0,z#=0.0)
	v.Vector = New Vector
	v\x=x
	v\y=y
	v\z=z
	Return v
End Function 


;// Magnitude
Function Magnitude#(v.Vector)
	Return Sqr(v\x * v\x + v\y * v\y + v\z * v\z)
End Function


;// Normalize
Function Normalize#(v.Vector)
	mag#=Sqr(v\x * v\x + v\y * v\y + v\z * v\z)
	v\x = v\x / mag
	v\y = v\y / mag
	v\z = v\z / mag
	
	If (Abs(v\x) < tol) v\x = 0.0
	If (Abs(v\y) < tol) v\y = 0.0
	If (Abs(v\z) < tol) v\z = 0.0
End Function


;// Reverse
Function Reverse(v.Vector)
	v\x = -v\x
	v\y = -v\y
	v\z = -v\z
End Function


;// Return Negative Vector
Function NegateVector(v1.Vector,v2.Vector)
	v1\x = -v2\x
	v1\y = -v2\y
	v1\z = -v2\z
End Function


;// Vector Addition
;// Form of Vector1 = Vector2 + Vector3
Function AddVector(v1.Vector,v2.Vector,v3.Vector)
	v1\x = v2\x + v3\x
	v1\y = v2\y + v3\y
	v1\z = v2\z + v3\z
End Function

;// Form of Vector1 = Vector1 + Vector2
Function AddVector2(v1.Vector,v2.Vector)
	v1\x = v1\x + v2\x
	v1\y = v1\y + v2\y
	v1\z = v1\z + v2\z
End Function


;// Vector Subtraction
;// Form of Vector1 = Vector2 - Vector3
Function SubVector(v1.Vector,v2.Vector,v3.Vector)
	v1\x = v2\x - v3\x
	v1\y = v2\y - v3\y
	v1\z = v2\z - v3\z
End Function

;// Form of Vector1 = Vector1 - Vector2
Function SubVector2(v1.Vector,v2.Vector)
	v1\x = v1\x - v2\x
	v1\y = v1\y - v2\y
	v1\z = v1\z - v2\z
End Function


;// Vector Scalar Addition
;// Form of Vector1 = Vector2 + Scalar
Function AddVecScalar(v1.Vector,v2.Vector,s#)
	v1\x = v2\x + s
	v1\y = v2\y + s
	v1\z = v2\z + s
End Function

;// Form of Vector1 = Vector1 + Scalar
Function AddVecScalar2(v1.Vector,s#)
	v1\x = v1\x + s
	v1\y = v1\y + s
	v1\z = v1\z + s
End Function


;// SubVecScalar
;// Form of Vector1 = Vector2 - Scalar
Function SubVecScalar(v1.Vector,v2.Vector,s#)
	v1\x = v2\x - s
	v1\y = v2\y - s
	v1\z = v2\z - s
End Function

;// Form of Vector1 = Vector1 - Scalar
Function SubVecScalar2(v1.Vector,s#)
	v1\x = v1\x - s
	v1\y = v1\y - s
	v1\z = v1\z - s
End Function


;// Vector Scalar Multiplication
;// Form of Vector1 = Vector2 * Scalar
Function MulVecScalar(v1.Vector,v2.Vector,s#)
	v1\x = v2\x * s
	v1\y = v2\y * s
	v1\z = v2\z * s
End Function

;// Form of Vector1 = Vector1 * Scalar
Function MulVecScalar2(v1.Vector,s#)
	v1\x = v1\x * s
	v1\y = v1\y * s
	v1\z = v1\z * s
End Function


;// Vector Scalar Division
;// Form of Vector1 = Vector2 / Scalar
Function DivVecScalar(v1.Vector,v2.Vector,s#)
	v1\x = v2\x / s
	v1\y = v2\y / s
	v1\z = v2\z / s
End Function

;// Form of Vector1 = Vector1 / Scalar
Function DivVecScalar2(v1.Vector,s#)
	v1\x = v1\x / s
	v1\y = v1\y / s
	v1\z = v1\z / s
End Function


;// Cross Product
;// Form of Vector1 = Vector2 |CrossProduct| Vector3
Function CrossProduct(t.Vector,u.Vector,v.Vector)
	t\x =  u\y * v\z  -  u\z * v\y 
	t\y = -u\x * v\z  +  u\z * v\x 
	t\z =  u\x * v\y  -  u\y * v\x 
End Function

Function CrossProduct2.Vector(u.Vector,v.Vector)
	tempx# =  u\y * v\z  -  u\z * v\y 
	tempy# = -u\x * v\z  +  u\z * v\x 
	tempz# =  u\x * v\y  -  u\y * v\x
	t.Vector=Vector(xtemp,ytemp,ztemp)
	Return t 
End Function


;// Dot Product
;// Form of Returns Vector1 |DotProduct| Vector2
Function DotProduct#(u.Vector,v.Vector)
	Return u\x * v\x + u\y * v\y + u\z * v\z
End Function


;// Set a Vector with new components
Function SetVector(v.Vector,x#,y#,z#)
	v\x = x
	v\y = y
	v\z = z
End Function


;// Vector 1 is set to Vector 2
Function CloneVector(v1.Vector,v2.Vector)
	v1\x = v2\x
	v1\y = v2\y
	v1\z = v2\z
End Function


;// Set a Vector to Zero
Function ZeroVector(v.Vector)
	v\x = 0.0
	v\y = 0.0
	v\z = 0.0
End Function


;// Kills a Vector
Function FreeVector(v.Vector)
	Delete v
End Function
