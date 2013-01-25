
data segment
	game_over		db	"***** GAME OVER *****",10,13,"$"
	racket		dw	?	;polozenie paletki
	ball_x		dw	? 	;pilka
	ball_y		dw	?	; jw
	vector_x	dw	?	; kierunek ruchu
	vector_y	dw	?	
	temp		dw 	? 	; 
data ends


stack1 segment STACK
	db 200 dup(?)
	top	db ? 
stack1 ends


code segment

	start:

		mov ax,seg top
		mov ss,ax
		mov sp,offset top

		mov ax,seg data
		mov ds,ax

		call set_param
		call graphic_mode ; tryb graficzny
		call print_racket ; pojawia nam sie paletka i pileczka
		call print_ball
		call main


	


	; wczytujemy klawisz coby wiedzieæ co robiæ
	keyboard:
		xor ax,ax
		mov ah,01h
		int 16h
		jz game
		xor ax,ax
		int 16h ; wczytuje znak, escape - wyjcscie,strza³ki lewo prawo ruch, pozosta³e klawisze nic
		cmp ah,01h
		je end_end
		cmp ah,4bh;
		je move_left
		cmp ah,4dh 
		je move_right
		jmp game
		
	; ustawiam odpowiednio zmienne
	set_param:
		mov word ptr ds:[racket],150
		mov word ptr ds:[vector_x],1
		mov word ptr ds:[vector_y],1
		mov word ptr ds:[ball_x],1
		mov word ptr ds:[ball_y],1
		ret
	; obsluga pileczki
	move_ball:
				mov ax,word ptr ds:[ball_x]	;sprawdzamy w pionie
				add ax,word ptr ds:[vector_x]
				mov word ptr ds:[ball_x],ax
				cmp ax,0					;gorna krawedz
				ja row_zero
				mov word ptr ds:[vector_x],1
				row_zero:
				cmp ax,197					;dolna krawedz
				jae end_end
				cmp ax,192  ; rakieta
				jb skip
				mov ax,word ptr ds:[ball_y]
				add ax,3
				cmp ax,word ptr ds:[racket]
				ja skip
				mov ax,word ptr ds:[racket]
				sub ax,39
				cmp ax,word ptr ds:[ball_y]
				ja skip
				mov word ptr ds:[vector_x],-1
				
				skip:
				mov ax,word ptr ds:[ball_y]
				add ax,word ptr ds:[vector_y]
				mov word ptr ds:[ball_y],ax
				cmp ax,0					;lewa krawedz
				ja column_zero
				mov word ptr ds:[vector_y],1
				column_zero:
				cmp ax,317	; prawa krawedz
				jb done
				mov word ptr ds:[vector_y],-1
				
				done:
				ret
		
	; obsluga paletki
	move_right:
		mov ax,word ptr ds:[racket] ;ruch w prawo
		add ax,5
		cmp ax,320 ; czy koniec
		jae right_border
		mov word ptr ds:[racket],ax ; jesli nie, zapisujemy nowe polozenie
		jmp game
		right_border:
			mov word ptr ds:[racket],320 ; zapisujemy nowe polozenie paletki
			jmp game
	
	move_left:
		mov ax,word ptr ds:[racket]
		sub ax,5
		cmp ax,40 ; jak poprzednio sprawdzam czy nie koniec i zapisuje paletke
		jbe left_border
		mov word ptr ds:[racket],ax 
		jmp game
		left_border:
			mov word ptr ds:[racket],40 
			jmp game
			

	sleep:
		xor cx,cx
		mov ah,86h
		mov dx,15000
		int 15h
		
	; rysowanie pileczki
	print_ball:
		mov ax,word ptr ds:[ball_x]
		call set_reg
		add di,word ptr ds:[ball_y]
		mov cx,4
		call line
		mov cx,4
		add di,316
		call line
		mov cx,4
		add di,316
		call line
		mov cx,4
		add di,316
		call line
		ret
		
	; rysuje paletke
	print_racket:
		mov ax,194
		call set_reg
		add di,word ptr ds:[racket]
		
		mov cx,5 ; dlugosc paletki
		print:
			add di,280
			push cx 
			mov cx,40 ; szerokosc paletki
			call line
			pop cx
			loop print
		ret	
	; rysuje sobie linie od dlugosci ktora sobie siedzi w cx
	line:
		mov byte ptr es:[di],al
		inc di
		loop line
		ret
	
	; procedura ustawia rejestry
	set_reg:
		mov word ptr ds:[temp],ax
		mov ax,word ptr ds:[temp]
		mov cx,320
		mul cx
		mov di,ax
		mov al,7
		ret
	
	graphic_mode:
		mov	ax,13h
		int 10h
		mov ax,0a000h
		mov es,ax
		ret
		
	text_mode:
		mov ax,03h
		int 10h
		ret
		
	; procedura wypisujaca napis "Koniec gry"
	end_game:
		mov dx,offset game_over
		mov ah,9
		int 21h
		ret
		
	end_end:
		call text_mode
		call end_game
		mov ax,04c00h
		int 21h

	clear:
		xor cx,cx ; lewy gorny rog
		xor bx,bx
		mov dx,63999 ; prawy dolny rog
		mov ah,06h	; przewijanie
		mov al,0	; czyszczenie
		int 10h
		ret
		
		main:
			call sleep
			call keyboard
			game:
				call move_ball
				call clear
				call print_racket
				call print_ball
			jmp main
		jmp end_end
		ret
code ends
end start
		
		