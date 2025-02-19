; 3D Verlet Functions

; from Thomas Jakobsen's "Advanced Character Physics"
; http://www.gamasutra.com/resource_guide/20030121/jacobson_01.shtml
; Adapted for Blitz3D by Chris "Miracle" Casey, May 6 2003

; What needs to be added:

; + Optimized collision checking - DONE pretty well
; + Verlet-to-world friction - DONE, but needs improvement
; + Converting these nine-sphere constructs into a single rendered cube
; + Better collision than just sphere-to-sphere
; + Adjustible friction per verlet and "springiness" per constraint

Include "vectorlib3d.bb"

Type VerletPack								; Contains everything we need for basic physics
	Field m.Vector							; Current position
	Field old.Vector						; Last position
	Field a.Vector							; Accumulated forces
	Field radius#							; Collision radius from center
	Field mass#								; Arbitrary mass unit
	Field entity							; Placeholder for the 3D sphere
	Field obj								; Which construct is this verlet a part of?
End Type

Type Constraint								; Two verlets connected by a mutual spring
	Field v1.VerletPack
	Field v2.VerletPack
	Field d#
End Type

Type cube
	Field v.VerletPack[8]
End Type

Global vGrav.Vector = Vector(0.0,0.5,0.0)	; Gravity
Global fTimeStep# = 0.1						; Increase = speed up, but lost accuracy
Global ITERATIONS = 3						; How many loops through the "relaxation" routine
											;    Lower = speed up / lose accuracy

Global vTemp1.Vector = Vector(0,0,0)		; Some temporary vectors for calculations
Global vTemp2.Vector = Vector(0,0,0)

Global xmax = 10								; These are the limits of the "world"
Global xmin = -10
Global ymax = 20
Global ymin = -5
Global zmax = 25
Global zmin = 5


Function TimeStep()							; The main loop
	AccumulateForces()
	DoVerlet()
	SatisfyConstraints()
End Function


Function AccumulateForces()					; As of right now, we only accumulate gravity
	For v.VerletPack = Each VerletPack
		CloneVector(v\a,vGrav)
	Next
End Function


Function DoVerlet()
	For v.VerletPack = Each VerletPack
		CloneVector(vTemp1,v\m)
		MulVecScalar(vTemp2,v\a,fTimeStep * fTimeStep)		; a * timestep * timestep
		AddVector2(v\old,vTemp2)							; old = old + a * ts * ts
		SubVector2(v\m,v\old)								; m = m - old + a * ts * ts
		AddVector2(v\m,vTemp1)								; m += m - old + a * ts * ts
		CloneVector(v\old,vTemp1)
	Next
End Function


Function SatisfyConstraints()
	For n = 1 To ITERATIONS
		For c.Constraint = Each Constraint
			SetDistance(c\v1,c\v2,c\d)
			CageVerlet(c\v1)
		Next
		For cc.cube = Each cube
			bing = 0
			q.cube = cc
			While bing = 0
				If q <> Last cube
					q = After q
					dp# = ((cc\v[0]\m\x - q\v[0]\m\x) * (cc\v[0]\m\x - q\v[0]\m\x)) + ((cc\v[0]\m\y - q\v[0]\m\y) * (cc\v[0]\m\y - q\v[0]\m\y)) + ((cc\v[0]\m\z - q\v[0]\m\z) * (cc\v[0]\m\z - q\v[0]\m\z))
					If dp < 6.0
						For aa = 0 To 8
							For bb = 0 To 8
								l# = cc\v[aa]\radius + q\v[bb]\radius
								dp2# = ((cc\v[aa]\m\x - q\v[bb]\m\x) * (cc\v[aa]\m\x - q\v[bb]\m\x)) + ((cc\v[aa]\m\y - q\v[bb]\m\y) * (cc\v[aa]\m\y - q\v[bb]\m\y)) + ((cc\v[aa]\m\z - q\v[bb]\m\z) * (cc\v[aa]\m\z - q\v[bb]\m\z))
								If dp2 < (l * l) 
									SetDistance(cc\v[aa],q\v[bb],l)
									SubVector(vTemp1,cc\v[aa]\m,cc\v[aa]\old)
									MulVecScalar2(vTemp1,0.4 * fTimeStep)
									AddVector2(cc\v[aa]\old,vTemp1)
								EndIf
							Next
						Next
					EndIf
				Else
					bing = 1
				EndIf
			Wend
		Next
	Next
End Function

	
Function SetDistance(v1.VerletPack,v2.VerletPack,dist#)	
		SubVector(vTemp1,v1\m,v2\m)
		deltalength# = Sqr((vTemp1\x * vTemp1\x) + (vTemp1\y * vTemp1\y) + (vTemp1\z * vTemp1\z))
		If deltalength <= 0.0 deltalength = 0.0001
		diff# = (deltalength - dist) / deltalength
		tmass# = v1\mass + v2\mass
		MulVecScalar(vTemp2,vTemp1,diff * (v2\mass / tmass))
		SubVector2(v1\m,vTemp2)
		MulVecScalar(vTemp2,vTemp1,diff * (v1\mass / tmass))
		AddVector2(v2\m,vTemp2)
End Function


Function CageVerlet(v.VerletPack)				; Maintains world limits
	col = False
		If v\m\x + v\radius > xmax
		v\m\x = xmax - v\radius
		col = True
	EndIf
	If v\m\x - v\radius < xmin
		v\m\x = xmin + v\radius
		col = True
	EndIf
	If v\m\y + v\radius > ymax
		v\m\y = ymax - v\radius
		col = True
	EndIf
	If v\m\y - v\radius < ymin
		v\m\y = ymin + v\radius
		col = True
	EndIf
	If v\m\z + v\radius > zmax
		v\m\z = zmax - v\radius
		col = True
	EndIf
	If v\m\z - v\radius < zmin
		v\m\z = zmin + v\radius
		col = True
	EndIf
	If col
		SubVector(vTemp1,v\m,v\old)
		MulVecScalar2(vTemp1,0.4 * fTimeStep)
		AddVector2(v\old,vTemp1)
	EndIf
End Function


Function Verlet.VerletPack(x#,y#,z#,radius# = 1.0, mass# = 1.0, obj%)
	v.VerletPack = New VerletPack
	v\m = Vector(x,y,z)
	v\old = Vector(x,y,z)
	v\a = Vector(0.0,0.0,0.0)
	If radius <= 0.0 radius = 0.01
	v\radius = radius
	If mass <= 0.0 mass = 0.01
	v\mass = mass
	v\obj = obj
	Return v
End Function

Function Constraint.Constraint(v1.VerletPack,v2.VerletPack,dist#)
	c.Constraint = New Constraint
	c\v1 = v1
	c\v2 = v2
	c\d = dist
	;Return c	; You may need this someday ... we don't for what we have here
End Function
