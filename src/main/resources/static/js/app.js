let currentPage = 0;
let totalPages = 1;
const pageSize = 5;
let toastTimeoutId = null;

function dismissToast() {
  const toast = document.getElementById("errorToast");
  if (toastTimeoutId) {
    clearTimeout(toastTimeoutId);
    toastTimeoutId = null;
  }
  if (toast) {
    toast.classList.remove("show");
  }
}

function showToast(message, variant) {
  const toast = document.getElementById("errorToast");
  const errorMsg = document.getElementById("errorMsg");
  if (!toast || !errorMsg) return;
  if (toastTimeoutId) {
    clearTimeout(toastTimeoutId);
    toastTimeoutId = null;
  }
  errorMsg.textContent = message;
  toast.classList.remove("toast-success", "toast-error");
  if (variant === "success") {
    toast.classList.add("toast-success");
  } else {
    toast.classList.add("toast-error");
  }
  // Force reflow to restart animation if quickly toggled
  void toast.offsetWidth;
  toast.classList.add("show");
  toastTimeoutId = setTimeout(dismissToast, 3500);
}

function loadUrls(page) {
  fetch(`/short-urls?page=${page}&size=${pageSize}`)
    .then((res) => res.json())
    .then((data) => {
      currentPage = data.number;
      totalPages = data.totalPages;

      const container = document.getElementById("urlContainer");
      container.innerHTML = "";

      if (data.content.length === 0) {
        document.querySelector(".pagination").style.display = "none";
        container.innerHTML = `<div class="empty">
            <svg width="88" height="88" viewBox="0 0 88 88" fill="none" xmlns="http://www.w3.org/2000/svg">
            <rect x="0.5" y="0.5" width="87" height="87" rx="43.5" fill="#18161D"/>
            <rect x="0.5" y="0.5" width="87" height="87" rx="43.5" stroke="#4B4257"/>
            <rect x="12" y="12" width="64" height="64" rx="32" fill="#3B2E4A"/>
            <path d="M39.9996 50.6672H37.3327C35.5645 50.6672 33.8686 49.9648 32.6183 48.7144C31.368 47.4641 30.6655 45.7682 30.6655 44C30.6655 42.2317 31.368 40.5359 32.6183 39.2856C33.8686 38.0352 35.5645 37.3328 37.3327 37.3328M48.0002 37.3328H50.6671C51.9053 37.3328 53.119 37.6776 54.1723 38.3285C55.2255 38.9795 56.0767 39.9109 56.6304 41.0183C57.1842 42.1258 57.4186 43.3656 57.3074 44.5987C57.1962 45.8319 56.7438 47.0098 56.0009 48.0003M38.6662 44H43.9999M30.6655 30.6656L57.3343 57.3344" stroke="#E9D5FF" stroke-width="2" stroke-linecap="round"/>
            </svg>
            <div class="text-group">
              <h3>No shortened links yet</h3>
              <p>Paste your first long URL in the field above to generate a short, trackable, and easy-to-share link.</p>
            </div>
            
          </div>`;
      } else {
        document.querySelector(".pagination").style.display = "";
        data.content.forEach((url, i) => {
          const shortUrl = window.location.origin + "/" + url.shortCode;
          const card = document.createElement("div");
          card.className = "url-card";
          card.style.animationDelay = `${i * 40}ms`;
          card.innerHTML = `
                            <div class="url-info">
                                <a class="short-url" href="/${url.shortCode}" target="_blank">${shortUrl}</a>
                                <div class="rigth-group">
                                  <span class="access-count">${url.accessCount} visit${url.accessCount !== 1 ? "s" : ""}</span>
                                  <button class="copy-btn" onclick="copyUrl(this, '${shortUrl}')">
                                    <svg width="14" height="14" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <g clip-path="url(#clip0_3_61)">
                                    <path d="M2.33302 9.33352C1.6913 9.33352 1.16626 8.80848 1.16626 8.16676V2.33296C1.16626 1.69124 1.6913 1.1662 2.33302 1.1662H8.16682C8.80854 1.1662 9.33358 1.69124 9.33358 2.33296M5.8333 4.66648H11.6671C12.3115 4.66648 12.8339 5.18886 12.8339 5.83324V11.667C12.8339 12.3114 12.3115 12.8338 11.6671 12.8338H5.8333C5.18892 12.8338 4.66654 12.3114 4.66654 11.667V5.83324C4.66654 5.18886 5.18892 4.66648 5.8333 4.66648Z" stroke="#E9D5FF" stroke-width="2" stroke-linecap="round"/>
                                    </g>
                                    <defs>
                                    <clipPath id="clip0_3_61">
                                    <rect width="14" height="14" fill="white"/>
                                    </clipPath>
                                    </defs>
                                    </svg>
                                    Copy
                                  </button>
                                </div>
                            </div>
                        `;
          container.appendChild(card);
        });
      }

      const pageInfo = document.getElementById("pageInfo");
      if (pageInfo) {
        pageInfo.innerText =
          totalPages > 0 ? `${currentPage + 1} / ${totalPages}` : "";
      }

      document.getElementById("prevBtn").disabled = currentPage === 0;
      document.getElementById("nextBtn").disabled =
        currentPage >= totalPages - 1;
    });
}

function copyUrl(btn, url) {
  let btnContent = btn.innerHTML;
  navigator.clipboard.writeText(url).then(() => {
    btn.textContent = "Copied!";
    btn.classList.add("copied");
    setTimeout(() => {
      btn.innerHTML = btnContent;
      btn.classList.remove("copied");
    }, 1500);
  });
}

function createShortUrl() {
  const input = document.getElementById("urlInput");

  if (!input.value.trim()) {
    showToast("Please enter a URL.", "error");
    return;
  }

  fetch("/short-urls", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ url: input.value.trim() }),
  })
    .then((res) => {
      if (!res.ok) throw new Error("Failed");
      return res.json();
    })
    .then(() => {
      input.value = "";
      showToast("Link shortened!", "success");
      loadUrls(0);
    })
    .catch(() => {
      showToast("Error creating short URL. Please try again.", "error");
    });
}

function nextPage() {
  if (currentPage < totalPages - 1) loadUrls(currentPage + 1);
}
function previousPage() {
  if (currentPage > 0) loadUrls(currentPage - 1);
}

document.addEventListener("DOMContentLoaded", () => {
  loadUrls(0);
  const closeBtn = document.querySelector("#errorToast .toast-close");
  if (closeBtn) {
    closeBtn.addEventListener("click", dismissToast);
  }
});
