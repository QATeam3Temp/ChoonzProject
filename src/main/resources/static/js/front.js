
const playlistbox = document.querySelector("#content");

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


setupPlaylists();


async function setupPlaylists(){
    
    let targets = (await sendHttpRequest("GET",`http://localhost:8082/playlists/read`))
    let i= 0;
    if(targets.length>0){
        playlistbox.innerHTML="";
    }
 
    targets.forEach(target => {
        if(i<6){
        let t = `
        <div id = "content" class="content">
        <a href="/playlist?id=`+target.id +`">
          <div class="content-overlay"></div>
          <img class="content-image" src="`+target.artwork+`">
          <div class="content-details fadeIn-bottom">
            <h3 class="content-title">`+target.name+`</h3>
            <p class="content-text">`+target.description+`</p>
          </div>
        </a>
      </div>     
        </a>
    </div>`
    playlistbox.innerHTML+= t;
    i++;
 }
    });
  }
