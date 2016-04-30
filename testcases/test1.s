	.text
tigermain:
tigermain_framesize=44
	subu $sp tigermain_framesize
L1:
	sw $ra -4+tigermain_framesize($sp)
	sw $s0 -8+tigermain_framesize($sp)
	sw $s1 -12+tigermain_framesize($sp)
	sw $s2 -16+tigermain_framesize($sp)
	sw $s3 -20+tigermain_framesize($sp)
	sw $s4 -24+tigermain_framesize($sp)
	sw $s5 -28+tigermain_framesize($sp)
	sw $s6 -32+tigermain_framesize($sp)
	sw $s7 -36+tigermain_framesize($sp)
	sw $s8 -40+tigermain_framesize($sp)
	sw $a0 0+tigermain_framesize($sp)
	li $v0 10
	move $a0 $v0
	move $a1 $0
	jal _initArray


	lw $s8 -40+tigermain_framesize($sp)
	lw $s7 -36+tigermain_framesize($sp)
	lw $s6 -32+tigermain_framesize($sp)
	lw $s5 -28+tigermain_framesize($sp)
	lw $s4 -24+tigermain_framesize($sp)
	lw $s3 -20+tigermain_framesize($sp)
	lw $s2 -16+tigermain_framesize($sp)
	lw $s1 -12+tigermain_framesize($sp)
	lw $s0 -8+tigermain_framesize($sp)
	lw $ra -4+tigermain_framesize($sp)
	b L0
L0:

	addu $sp tigermain_framesize
	j $ra
