const chatBox = document.getElementById("chat-box");

function addMsg(text, type) {
  const div = document.createElement("div");
  div.className = "msg " + type;
  div.innerText = text;
  chatBox.appendChild(div);
}

function sendMessage() {
  const input = document.getElementById("input");
  const text = input.value;

  if (!text) return;

  addMsg(text, "user");

  setTimeout(() => {
    addMsg("Resposta simulada (adicione IA depois)", "bot");
  }, 500);

  input.value = "";
}

async function generateImage() {
  const input = document.getElementById("input");
  const prompt = input.value;

  if (!prompt) return;

  addMsg("Gerando imagem...", "user");

  const response = await fetch("http://localhost:8080/generate", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ prompt: prompt })
  });

  const data = await response.json();

  const img = document.createElement("img");
  img.src = data.output[0];
  img.style.maxWidth = "300px";
  img.style.margin = "10px";

  chatBox.appendChild(img);

  input.value = "";
}

function clearChat() {
  chatBox.innerHTML = "";
}
