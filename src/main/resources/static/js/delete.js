var status;
const targetSelector = document.querySelector("#swordOfDamocles");

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}

function sendHttpRequest(method, url) {
  let headers = {
    "Content-Type": "application/json",
    "Key":document.cookie.split("=")[1]
  }
  console.log(headers);
    return fetch(url, {
      method: method,
      headers: headers,
    })
      .then((response) => {
        status = response.status;
        if (response.status >= 200 && response.status < 300) {
          
          return response.json();
        } else {
          if (response.status == 401) {
            alert("You are either not logged in or have an invalid User Key. Please log in to fix this.  If this issue persists, please contact support.");
          }
          return response.json().then((errData) => {
            console.log(errData);
            throw new Error("Something went wrong - server-side");
          });
        }
      })
      .catch((error) => {
        console.log(error);
        throw new Error("Something went wrong");
      });
  }

  function sendHttpDeleteRequest(method, url) {
    let headers = {
      "Content-Type": "application/json",
      "Key":document.cookie.split("=")[1]
    }
    console.log(headers);
      return fetch(url, {
        method: "DELETE",
        headers: headers,
      })
        .then((response) => {
          status = response.status;
          if (response.status >= 200 && response.status < 300) {
            
            return response;
          } else {
            if (response.status == 401) {
              alert("You are either not logged in or have an invalid User Key. Please log in to fix this.  If this issue persists, please contact support.");
            }
            return response.json().then((errData) => {
              console.log(errData);
              throw new Error("Something went wrong - server-side");
            });
          }
        })
        .catch((error) => {
          console.log(error);
          throw new Error("Something went wrong");
        });
    }

  function load(){
    setupTargets()
  }

  function deleteClick(){
    deleteTarget()
  }
    
  async function setupTargets(){
    
    let targets = (await sendHttpRequest("GET",`http://localhost:8082/`+getUrlVars()["x"]+`/read`))
    targets.forEach(target => {
     let t = "<option value="+ target.id + ">"+target.name + "</option>"
    swordOfDamocles.innerHTML+= t;
    });
  }

  async function deleteTarget(){
    
    await sendHttpDeleteRequest("DELETE",`http://localhost:8082/`+getUrlVars()["x"]+`/delete/`+swordOfDamocles.value)
    setupTargets();
    
  }