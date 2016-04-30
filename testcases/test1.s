	.text
tigermain:
tigermain_framesize=44
	subu $sp tigermain_framesize
L1:
	addu $v0 $sp tigermain_framesize
	li $v0 -4
	add $v0 $v0
	sw ($v0) $ra
	addu $v0 $sp tigermain_framesize
	li $v0 -8
	add $v0 $v0
	sw ($v0) $s0
	addu $v0 $sp tigermain_framesize
	li $v0 -12
	add $v0 $v0
	sw ($v0) $s1
	addu $v0 $sp tigermain_framesize
	li $v0 -16
	add $v0 $v0
	sw ($v0) $s2
	addu $v0 $sp tigermain_framesize
	li $v0 -20
	add $v0 $v0
	sw ($v0) $s3
	addu $v0 $sp tigermain_framesize
	li $v0 -24
	add $v0 $v0
	sw ($v0) $s4
	addu $v0 $sp tigermain_framesize
	li $v0 -28
	add $v0 $v0
	sw ($v0) $s5
	addu $v0 $sp tigermain_framesize
	li $v0 -32
	add $v0 $v0
	sw ($v0) $s6
	addu $v0 $sp tigermain_framesize
	li $v0 -36
	add $v0 $v0
	sw ($v0) $s7
	addu $v0 $sp tigermain_framesize
	li $v0 -40
	add $v0 $v0
	sw ($v0) $s8
	addu $v0 $sp tigermain_framesize
	add $v0 $0
	sw ($v0) $a0
	li $v0 10
	move $a0 $v0
	move $a1 $0
	jal _initArray
	lw $v0 $v0
	lw $v0 $v0
	addu $v1 $sp tigermain_framesize
	li $v1 -40
	add $v1 $v1
	lw $s8 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -36
	add $v1 $v1
	lw $s7 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -32
	add $v1 $v1
	lw $s6 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -28
	add $v1 $v1
	lw $s5 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -24
	add $v1 $v1
	lw $s4 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -20
	add $v1 $v1
	lw $s3 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -16
	add $v1 $v1
	lw $s2 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -12
	add $v1 $v1
	lw $s1 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -8
	add $v1 $v1
	lw $s0 ($v1)
	addu $v1 $sp tigermain_framesize
	li $v1 -4
	add $v1 $v1
	lw $ra ($v1)
	b L0
	la $v1 L0
	jr $v1
L0:

	addu $sp tigermain_framesize
	j $ra
