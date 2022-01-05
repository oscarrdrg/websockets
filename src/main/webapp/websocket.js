window.onload = init;
var socket = new WebSocket("ws://localhost:8080/websocketexample/actions");
socket.onmessage = onMessage;

function onMessage(event) {
    var intercambio = JSON.parse(event.data);
    if (intercambio.action === "add") {
	 	console.log('add');
        printIntercambio(intercambio);
    }
    if (intercambio.action === "remove") {
		console.log('remove');
        document.getElementById(intercambio.id).remove();
    }
    if (intercambio.action === "update") {
       console.log('update');
		updateIntercambio(intercambio);
}
}

function addIntercambio(kpi1, kpi2) {
    var IntercambioAction = {
        action: "add",
        kpi1: kpi1,
        kpi2: kpi2,
    };
    socket.send(JSON.stringify(IntercambioAction));
}

function removeIntercambio(element) {
    var id = element;
    var IntercambioAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(IntercambioAction));
}

function printIntercambio(intercambio) {
	console.log("El id inicial es: "+intercambio.id)
    var content = document.getElementById("content");
    
    var intercambioDiv = document.createElement("div");
   	intercambioDiv.setAttribute("id", intercambio.id);
    intercambioDiv.setAttribute("kpi1", intercambio.kpi1);
    intercambioDiv.setAttribute("kpi2", intercambio.kpi2);
    intercambioDiv.setAttribute("valor", intercambio.valor);
    intercambioDiv.setAttribute("date", intercambio.date);
    intercambioDiv.setAttribute("class", "device Lights");
    content.appendChild(intercambioDiv);

	 var kpi1 =document.createElement("span");
    kpi1.innerHTML = "KPI 1: " + intercambio.kpi1;
    intercambioDiv.appendChild(kpi1);
    
    var kpi2 =document.createElement("span");
   	kpi2.innerHTML = "KPI 2: " + intercambio.kpi2;
    intercambioDiv.appendChild(kpi2);
    
    var valor =document.createElement("span");
    valor.innerHTML = "Valor del intercambio: " + intercambio.valor;
    intercambioDiv.appendChild(valor);
    
    var date =document.createElement("span");
    date.innerHTML = "Date: " + intercambio.date;
    intercambioDiv.appendChild(date);
    
    var removeIntercambio = document.createElement("span");
    removeIntercambio.setAttribute("class", "removeDevice");
    removeIntercambio.innerHTML = "<a href=\"#\" OnClick=removeIntercambio(" + intercambio.id + ")>Borrar intercambio</a>";
    intercambioDiv.appendChild(removeIntercambio);
}

function showForm() {
    document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
    document.getElementById("addDeviceForm").style.display = "none";
}

function formSubmit() {
    var form = document.getElementById("addDeviceForm");
    var kpi1 = form.elements["kpi1"].value;
    var kpi2 = form.elements["kpi2"].value;
    hideForm();
    document.getElementById("addDeviceForm").reset();
    addIntercambio(kpi1, kpi2);
}

function init() {
    hideForm();
}

function updateIntercambio(intercambioActualizado){
    console.log("El valor actualizado es "+intercambioActualizado.valor);
    console.log("La fecha actualizada es "+intercambioActualizado.date);
    console.log("El id actualizado es "+intercambioActualizado.id);
    var aux=document.getElementById(intercambioActualizado.id);
    var valorActualizado = aux.children[2];
    var dateActualizada = aux.children[3];
    valorActualizado.innerHTML = "Valor del intercambio: " + intercambioActualizado.valor;
    dateActualizada.innerHTML = "Date: " + intercambioActualizado.date;
}



