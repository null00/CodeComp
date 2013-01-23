main:	
; initializes the two numbers and the counter.  Note that this assumes

	mov	ax,'00'		; initialize to all ASCII zeroes
	mov	di,counter		; including the counter
	mov	cx,digits+cntDigits/2	; two bytes at a time
	cld			; initialize from low to high memory
	rep	stosw		; write the data
	inc	ax		; make sure ASCII zero is in al
	mov	[num1 + digits - 1],al ; last digit is one
	mov	[num2 + digits - 1],al ; 
	mov	[counter + cntDigits - 1],al

	jmp	.bottom		; done with initialization, so begin