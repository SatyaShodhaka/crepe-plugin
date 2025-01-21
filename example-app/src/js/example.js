import { Crepe } from 'crepe-plugin';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    Crepe.echo({ value: inputValue })
}
