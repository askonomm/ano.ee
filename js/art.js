function setup() {
    createCanvas(window.innerWidth - 700, window.innerHeight);
    background.apply(null, [245, 245, 245]);
    noLoop();
    stroke(255);
    angleMode(DEGREES);
}

function draw() {
    translate(width/2, height/2);
    rotate(45/2);
    branchComponent(115, random(2, 15), random(10, 60));
}

window.addEventListener('resize', () => {
    resizeCanvas(window.innerWidth - 700, window.innerHeight);
});

function branch(len, angle, gen) {
    line(0, 0, 0, -len);
    translate(0, -len);
    len *= 0.6;
    angle = random(angle-10, angle+20);

    if (len > 2) {
        push();
        rotate(angle);
        branch(len, angle, gen);
        pop();

        push();
        rotate(-angle);
        branch(len, angle, gen);
        pop();
    }
}

function branchComponent(len, amount, angle) {
    stroke.apply(null, [111, 105, 172]);
    var increment = 360/amount;
    var rotAmount;

    for (var i = 0; i < amount; i++) {
        push();
        rotAmount = -180 + increment * i
        rotate(random(rotAmount - 60, rotAmount));
        branch(len, angle, 1);
        pop();
    }
}