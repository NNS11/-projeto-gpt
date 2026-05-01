async function sendMessage() {
  const input = document.getElementById("input");
  const msg = input.value;

  if (!msg) return;

  addMsg(msg, "user");

  const res = await fetch("http://localhost:8080/chat", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ msg })
  });

  const data = await res.json();

  console.log(data);

  addMsg(data.output || "Erro na resposta", "bot");

  input.value = "";
}
