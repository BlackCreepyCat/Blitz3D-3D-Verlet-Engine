Include "verletfast.bb"

Graphics3D 800,600,0,2
SetBuffer BackBuffer()
cam = CreateCamera()
lgt = CreateLight()
PositionEntity cam,0,0,-3

flr = CreateCube()							; Making a "floor" just so we can see it
ScaleEntity flr,10,0.01,10
PositionEntity flr,0,-5,15

For m = 1 To 50								; Let's make some cubes!
	xa# = Rnd(-9,9)
	xb# = Rnd(-2,18)
	xc# = Rnd(4,24)
	
	cc.cube = New cube

	; Yes, all the below is necessary to create a stable 9-verlet "cube" ...
	a.VerletPack = Verlet(xa - 0.5, xb - 0.5, xc - 0.5, 0.5, 1.0, m)
	b.VerletPack = Verlet(xa + 0.5, xb - 0.5, xc - 0.5, 0.5, 1.0, m)
	c.VerletPack = Verlet(xa + 0.5, xb + 0.5, xc - 0.5, 0.5, 1.0, m)
	d.VerletPack = Verlet(xa - 0.5, xb + 0.5, xc - 0.5, 0.5, 1.0, m)
	e.VerletPack = Verlet(xa - 0.5, xb - 0.5, xc + 0.5, 0.5, 1.0, m)
	f.VerletPack = Verlet(xa + 0.5, xb - 0.5, xc + 0.5, 0.5, 1.0, m)
	g.VerletPack = Verlet(xa + 0.5, xb + 0.5, xc + 0.5, 0.5, 1.0, m)
	h.VerletPack = Verlet(xa - 0.5, xb + 0.5, xc + 0.5, 0.5, 1.0, m)
	i.VerletPack = Verlet(xa, xb, xc, 1.0, 2.0, m)

	; Edges - distance 1.0
	Constraint(a,b,1.0)
	Constraint(b,c,1.0)
	Constraint(a,d,1.0)
	Constraint(c,d,1.0)
	Constraint(e,f,1.0)	
	Constraint(f,g,1.0)
	Constraint(e,h,1.0)
	Constraint(g,h,1.0)
	Constraint(a,e,1.0)
	Constraint(b,f,1.0)
	Constraint(c,g,1.0)
	Constraint(d,h,1.0)

	; Cross-faces - distance ~Sqr(2.0)
	Constraint(a,c,1.41421)
	Constraint(b,d,1.41421)
	Constraint(e,g,1.41421)
	Constraint(f,h,1.41421)
	Constraint(a,f,1.41421)
	Constraint(a,h,1.41421)
	Constraint(b,e,1.41421)
	Constraint(b,g,1.41421)
	Constraint(c,f,1.41421)
	Constraint(c,h,1.41421)
	Constraint(d,e,1.41421)
	Constraint(d,g,1.41421)

	; Diagonals - distance ~Sqr(3.0)
	Constraint(a,g,1.73205)
	Constraint(b,h,1.73205)
	Constraint(c,e,1.73205)
	Constraint(d,f,1.73205)
	
	; Constraining the center sphere - distance ~0.5 * Sqr(3.0)
	Constraint(a,i,0.866025)
	Constraint(b,i,0.866025)
	Constraint(c,i,0.866025)
	Constraint(d,i,0.866025)
	Constraint(e,i,0.866025)
	Constraint(f,i,0.866025)
	Constraint(g,i,0.866025)
	Constraint(h,i,0.866025)
	
	cc\v[0] = i
	cc\v[1] = a
	cc\v[2] = b
	cc\v[3] = c
	cc\v[4] = d
	cc\v[5] = e
	cc\v[6] = f
	cc\v[7] = g
	cc\v[8] = h
Next
	
For g.VerletPack = Each VerletPack			; Making the spheres you see
	g\entity = CreateSphere(4)
	ScaleEntity g\entity,g\radius,g\radius,g\radius
	PositionEntity g\entity,g\m\x,g\m\y,g\m\z
	EntityColor g\entity,Rnd(200,255),Rnd(200,255),Rnd(200,255)
Next

While Not KeyHit(1)

	Framecounter_counter=Framecounter_counter+1
	If Framecounter_time=0 Then Framecounter_time=MilliSecs()
	If Framecounter_time+1001 < MilliSecs() Then
		Framecounter_framerate#=Framecounter_counter
		Framecounter_counter=0
		Framecounter_time=MilliSecs()
	EndIf

	TimeStep()
	For v.VerletPack = Each VerletPack
		PositionEntity v\entity,v\m\x,v\m\y,v\m\z
	Next
	RenderWorld()
	Text 10,10,"FPS: " + Framecounter_framerate
	Flip

Wend
End