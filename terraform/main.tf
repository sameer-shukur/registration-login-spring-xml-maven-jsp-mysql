provider "aws" {
access_key = "${var.access_key}"
secret_key = "${var.secret_key}"
region = "us-west-2"
}


resource "aws_security_group" "default" {
  name = "terraform_example"
  description = "Used in the terraform"

  ingress {
    from_port = 22
    to_port = 22
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
}

  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/16"]
}

  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
}
}

resource "aws_instance" "ubuntu" {
  ami = "${var.ami}"
  instance_type = "t2.micro"
  key_name = "${var.key_name}"
  vpc_security_group_ids = ["${aws_security_group.default.id}"]
  tags = {
    Name = "ubuntu_provisioned"
  }

}

output "ip" {
value = "${aws_instance.ubuntu.public_ip}"
}

