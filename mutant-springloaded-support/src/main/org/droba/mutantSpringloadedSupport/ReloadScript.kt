package org.droba.mutantSpringloadedSupport

import kotlinx.html.*

fun FlowContent.reloadScript() = script {
    unsafe {
        //language=JavaScript
        +"""
                    var socket_open = false;
                    var socket_retry_timeout_id = null;

                    createMutantWS();

                    function scheduleRetry() {
                        if (socket_open)
                            return;

                        socket_retry_timeout_id = setTimeout(createMutantWS, 500);
                    }


                    function createMutantWS() {

                        if (socket_open)
                            return;

                        var reloadSocket = new WebSocket(url("/"));

                        reloadSocket.onopen = function(e) {
                            console.log("I'm open baby");
                            socket_open = true;
                        };

                        reloadSocket.onmessage = function(e) {
                            console.log(e);
                            if (e.data === 'MutantWS Reload')
                                window.location.reload(true);
                        };

                        reloadSocket.onclose = function(e) {
                            console.log("Closed!", e);
                            socket_open = false;
                            scheduleRetry();

                        };

                        reloadSocket.onerror = function(e) {
                            console.log("Error!", e);
                        };
                    }

                    function url(s) {
                        var l = window.location;
                        return ((l.protocol === "https:") ? "wss://" : "ws://") + l.host + s;
                    }
                """
    }
}

fun FlowContent.liveReloadConnectionImage() = img(src = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAAB1CAYAAAC4aERqAAAAAXNSR0IArs4c6QAAAAlwSFlzAAALEwAACxMBAJqcGAAAAVlpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IlhNUCBDb3JlIDUuNC4wIj4KICAgPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4KICAgICAgPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIKICAgICAgICAgICAgeG1sbnM6dGlmZj0iaHR0cDovL25zLmFkb2JlLmNvbS90aWZmLzEuMC8iPgogICAgICAgICA8dGlmZjpPcmllbnRhdGlvbj4xPC90aWZmOk9yaWVudGF0aW9uPgogICAgICA8L3JkZjpEZXNjcmlwdGlvbj4KICAgPC9yZGY6UkRGPgo8L3g6eG1wbWV0YT4KTMInWQAAIYRJREFUeAHtmge4ZlV1hjUFBBSQ3qUjoKIRpIwFBKOABTFgpCOWYIkozWiQpoCKqFgi2OhogESqBZCOgA8oIH2oUkVQQZoxJt/7n/3eWRz+OzCXYebewfU839lrr7322qvsvc/578xznzM+6G/iBvhLQ/VqwXSWD1YIlguWCBYK5g1eEMwRQI8FDwa/D34b3BHcFFwfTA7uDCo9N52/DYatWfVmKI9TM5MoAkn5n+LEXOFXD14XTApWDhYL0Bsr3Z2J1wUXBecEFwf3B9Lfhfm/4H8VzKx2ZhXEE/HnEvirw28avCmgCH1iJ5MwWpIH8N8Y6Fey2MMKeXMUzwhOCM4OOF3QuClM584z/yR5BC09P8wOwXmBSbYlSY8EtBRC+bS2zP1T8GhDLSi2Lgs+FnANSvhIQWc4ubtmxMIE6Yng3qcQHwxeHEgkjgSi+/cKW8vYXcHtreU98UBAoqHZAt4pCwSLBEsEiwZzBpXwARD77GUAu4cG/xFgG6o+d5JZ4EngNbnvSP+KwJ3OjuUUkNi6e3mvsHu/FmwTvCKYL3iqNE8UVw02Dw4MLgweDlyXljX7J/CWyN4fSBRlppwWHZieLfe3p3DZ8NzZJoRdyq4HymgvD/YKXhWw64cRNus7gnUAMtcL+zhi7KXBLgHFqWvqB5tA+dnh+biQKMyEphoAO5xrgGA5BSSgBo/8lOCtwfOCSiSaE+ZOHS3hdY4FYw5zsVGJ4qwXHB1wSiyCfrFZkHF6dg+ketKVTYhWx0kId3INmGBrMU5Lf92gEgm0AFVOoh1jjWFgHjr9wlEEx8KOEFfhUYE+4p+nV9kPIps/gIyt602Apw7zYj0vIChOBUWohbg6/U2DSv0ikESSOyzBdd5ovAVkPrYki1OL9roMnh9YBHy1OMiuC7jyIPyscwfC6fGY3kYpBoHwO+KkYPnWJyF8PVmsg8LvGfwxgJATPIGbRIoIKvFVtFCwcLBAMHeAjHlcL38IuBrvCX4T4EslC+tnNGOsTZ+1WHuX4NMB7y98oni09LH/9uCsgKJUO+mOLzLZq8WtOwOTRKC+uO8Iv3EgMcdNQasNx/k8Xiv4WHBs8Mvg3qCeNNapYC0KcmlwZPDh4JVBte1ao629RvSvDLBLMUi8MfDO2SiAKMq4JIPlZJAMi0Eg7vzzwi8ZQF5FXe+Jga2egQOCywN3YU06vLYpDjBxfT36jP882Dvw2gk7oJpUrzIGnh98P2A+m4r1LAr23hBAxt71xsFTh0j2zQEBcH0YBH12t4Fz9N2ZXCEkQWLnnRoYOHO193D4RwKSoXxY64lElzl9W8iOD9YLpL4f+Ch9Lozr1KI8FDknCTIHXW8mPgkE4lP1Z4HJq8X4BgqNquOVXzvjpwUGTkviSSpXRJXLk2judK4w3hvwoxULG9jytGrjhMheFkhuGvqV/0T6zqlFuTVyNiJU9TvJDH66y1n28ACHSRIOG/jXw0s6zDz5OcIfGFBAA8YGJww7ymivCo4Idg74vcLuXClYugF+zYCX7m7BMcF1QbWBX9hmDeUUaq/Ak6pvEQ2+7mghbDoH37BD/+zAuW7QiGY86fgHsjSOeYe7S48sLqlLMXT6H8LzkjZIksTcWojJ6fMumRTMFUwr8RW2XvCl4LbAtfCVtWphzk9/xQDCR3yF9Bd+nwAb+AiM9aDwEIVx3kAwox4m+CVZkE9XC2KAZ0TmrlG3Ort5xrmDmcccEuRcZLzM3xPw96g+YY/rjnYYGKtJTHdAfCbzxXV9wBqAhFoc+lx9bwyg0YrynYzVudracDBryulv3We+MdGsROJxiGS6W24Ov3AA+Z6oxfhg5AZhMTwV92Xso8HsgcRcklNtsAsBsj4cc9y5UR3QC/LcI6gbifW9gvBtywCqa1J8CHvnB8btRuJ65ERC6Mww0rH3Z0WcIhh2GTzvgtcFkF8qJgbZvwboAXen/VMiWyaQKCYJkQiStavMsX6LDrrMYX0ImRuE/qrBOYHrE4fJRbZVAFUbzl8uck6TcTjvs0wIMWeGkAtx/G8NdMiC7NW8sGh05QnQ4PvF2LPNo+kXwsQWlQGL3rwBpxHMF9STle6ALISFIQZ9QuGLgX4Rhycd2cYBZCEqbzxsQuc8HP6lKIXqGp3kGXhakL1iG4dxxN1xcXjHbQ1k7abrHAMgmK0DSX36BGQS6b8w4H7/VHBccEkwObgjuDO4Kbg0ODHYL3hbsEhQqSaprvXhKNWiGNPvI1+lGXBu9Qk/+jEd3fTRq7pNPP0ak7xYTN4d4IiOw68bQAaqPjv3+kB9diE87SYBhK76BCHP2OrBV4MbA+ZNCyjW4cHrA4kTAyDWMWnbhdc2vhnbz8N78vTL4iyfsd+1eeqz2fzBqF5E0580/smYxvF6OggaIrgKZN8O0Mdh7mmD/ufwEHZNigEj5xP0qMBAnUefLzTADz5exgBeOX31bU+NbK1AMh6K47ofDK8+vrq27wZ09dX5e7Y5NR/EDJmLrjcdn+6o58fmtQFOGzT3Jp+/kE56SjaKzADZdRaEH1lQ3aHORU5iHgicS7AmX9mTtSSTOSYVfa7IfQNJP2uiST66+Auct3Z4SD8tIv9OcmuAnjnhqlsugNTretPpqePvjD0dNFB3g0WzxXGOO/roklT47wVS1UWG84cF6NV5JgYZQfO+OiT4ePC+4L3BLsFXgnOCWkw2Qd292PhJQCIhY6uJ+3Hk6NV5zJE8Jc7Bj74+Mkidrjcdni6OqRMCk0JLotYKIANzB20XGTomBJ6dtFAAqafDzD850C7JsIjO3Sey1QLnhh1KXHc7B1cFzAXYwl830mXh+74Yw/IZuz9wnjb4UIDU0/clIrsnQO/R1rIZ9bPmMOKnRxp9Ucz47e2ipxfTLOqOx2FPBzuaqwJntwogA1If2TEBOiSsFhGea8QdHXZArEdCKvqBz5FxCuOJoSj4gk+sdUHAH0Yhk6tv/EBFh/UtYo1X3205sfpPyzrrBJA57HpP86mxbWLHhQgM/t3Ntjq2b2nj1cEzmy6NQZiETzZ9dnAtxs3pvzaQsM8c5yuvLWPomFjGVg7cIKxRi/ItFBr1NxWniBgsIPyrm66xus76TbfGvE9Pt3XH3tQd99224COtvTftks20iTVRxzadGggveMgADGjNyEgSgdBa7MvDLxFAFqLrdV8vyLDF2hZAm+rhj+vNGf60gHVYg8JTGPq8GyF1tbNVZIyjayxfCQ+Rm1pATtoVAfreIOeEl8yN/TG1Gpkrs72P+bRk0R8UizhmUUii96lBXFB0sYm+9JMw2ONasBiTwy8eQLN1zciTZNX5IwONqb44pg0Sfk7AeqzleteG5wsSIg7jpoi/CtB3I+LbvAGkngX8UmToWpD7wi8bQOp0vTE+TTIvUpPrffqRZlMdd9cWkeMUu+/Pjf9AWkgdnXtzZOqaHAJfA+WQP8jg+4leNbL3BPsGewfbB7zMJfxijmRRFongtoB1WVMfdwoP6ZtxfTIydI0b/h8DSF1bXvqMc6JowVsDSJ2uN8anRkyySSPZ6zSb6rhbDokcRywgu2TpADLIrtf9mUNdE7NrG7R4dEmsyX15+P8KHg4M2paX91GBhcEn54UdOW1vD+8c/fxlZL7gmWdcrOfpcOd/JjJIHeNaKjI/fLS7x0DzibE38bQ1LrRvphGAjt0afsFmCh2LQRJ9eZow7u1K2uRF+8cAuzr/i/DuZPVqMd5TdJnHTmSTAN8HyP8Q/FMA1aLU4lDUuja8O7/GxPyLmq7xn56+VP1D98IAW8Z/jIpp6/pFPIU1kVMkj+cIElqhawYLwd4U8FKHWNyFFg+/LMKQsvO77sjpcM0NIufdRFLdaV8Pz9VAYZFD6LPGh4JvBhQMHYqAnLkAX5ExNndwXPCuALlrqh/Rc77MI4Q9NgS0YdcM7KJLYZj/sybXzorp+xmuf8SL7jVNl/nQMkHdXAPhaA8XGDbOAhol0ZBJvrnrDvroKF86/HxtjEAhdn0lriboNV0zuMPx4/bgxCYjMIhEU5h1A79uSDhyimagYQc8MsbQgb4TcOXUomv7vMhNtLK1I7MIYUcKeRmdELahhYOlBtyU2M3l5Ca3j+68Pd3WfWLjpCeOTFmIrw+vJ/V/3SbgPAWxcEs2OUlHlyvppiajQYbuHMEqQaXz0/lNgA4JpMUOa+wfQCSapDPGnc/HwuuDDYKdgusDxiwK74T9AghbbBySb9JPDw/Rh1YIlhhwna5xYVefkPGxUfXalEHz69bR5gvTtyBVbyhvxYcNuuu5Vl7QFAgWInGQOl1vyj/duiO51gBEIMwnIXzpLBpA2ry4644kS903Rb5WwHzWA+cGXC/c09KZYY4OzghWC/QNvXWCCwOSZGHCDv5NhdbTzOlm598asL4FuTv87wOuKQvKzq/kesZrbueMElfoUyKTMTVlDLLTIBfFuUo67k7AaQi9hwbc4x8LpGuR3UnswkrafGMT/iktpwP6VEAxOGnICB6eL5xPBxAykg9t0DUjCW7d59wSRv/UZbNI+sBHAoCMjZ0/jPjSQ4dcMZ9is6mfEk2tICafgE2EskdGsU5SIAMhaSSyTxRDm/hAAHweQ87llEErdM2I/Pb0r2kybPMiJ5muwzuLpOBr3wZ9ExV2sGEehAm5bt3Nynjp88kLKXOT2u9GOz3XdcwTqM6o7dQK4iR0LIStu0Qd27499HTKFl12byUCILGS69Dnvq5EctzNVa597FgcZX0bzsOOttTVt+pDjcO5/ViVD9P1FlBn1HY0o3UCybIAfaerHryJUM7OGBYgSa3EafF0IWcdE3IPgkLc3V4XzEMPuAsXCl8/SdMd/CmHFr1qm0JZLNfTt6rHOgBSrx9rN9rF28+rRVdn1LY/sSqafBZ2cWWj3Yl8VUHa5fh7tAcD7cF97BXg8V60jRmwu+qiJqePLl99WzYZNvAJaG/b8NjgpJjEi8ND2ux63dfjPK2jz/c7WFriNWb9M1b7qvPOdRNq0yKrM2rrhFEVMsBLz3eGJ8XfGhZIp+5thrTLTvVFz5D6fKX5YWBBVkUhpI7tiZGhS3Jdf4/wOwV+GIQdrLNnWn5AQszHr9uCHwaQNrte9ycWThZ2LfidbRDdYXEoM9amPtJYYH1lo/ieGlEajdH4sHGdZyewoyFli3Tdkb4FuaPJ3Zn8flm8ydBx/t3hSRSkbFLXHdzp6FIo/LslODiQkLMDvxhcHpwenBlcEewVQOh4Gr4Qng8G5iDXdtiRf9vgNEF9v4xr6YxxQvDVnFm4iAZkHObGa6rmT90xtTrD5DMCFuSk0H4vkNDziK4WXh2uOXTfHUDqmKhDImOc40zLLlo5gNQ1eAp8VqA+ttmB9CtIuFcs8hMCSVuuz06+IUCPr0HaHwdSjWv3CBk3Jgq8dABpT/v7RlZtXpu+X27qMm8oaWTYIEYdv60pIIOWDUwajiu/NbynxB2yVmSQR1ibZ3XigR10eTds1WTaYw7rsIM3Cy4MuGIoEDrMExQDYgxwTW0dQCTC9fEXeluwfOAayM7mETJx2IbW7JoRGzVO/MCm9ldsusZwZ/p8hkPKut4YnibdHfJoM8r9+aJmD+cNEtFJAQs/0tqr0/KigyiGBeFriOKh6ynB7lIB5NqV50vsK4G2SQLzgTwnbb9An0xuRCOJ5suK666ujQ+vCCDmOG/h8LcH6Lrud8NDxmKLf1yd6HrqDg0P4Y8+DQRjeZiUt2Qyi7ALacGGAaSO7ccjY5xdre4bwkPsXMgAvhoeHa4C9Y8IL9UATBBjOwTaphAWA9kmgVTnILO/Z3h0OQFeQz8ML+Gf8WweHl3W0EfWh9TR7qqRWQh1PzLQnLJ2646tcaHlMp0vHRzzlOzbTJpck71m5DpjsN9qutqzfVmx5xzWeG9Pv3VHEnBwBCbUYjj/U01Zf/pz14/AjeUcbL21zKsb4fjIa9ycQK8l43Ct7ZtutbtuZJDF63pjfJpsFr4wwDE+g2nPD3ScVl3aS4IaBC/BZQJIx9TnCkKX4rFj4cGbAkh925Ui04eqL39PxhdlYsiE2a4cGfc59kmaiTstvEQs6vOR4nXqRqwnyfiN5ejoY1vd28LzpQlps+uN8VkT/fnYYDEdpMVhyMVM2u6R9YPef6A5Rdc5OHxL0zfRV6XvLjRYW+xg29PHCREWZbfIIHeuc+eP7MyA+Y+0liuGkwrpv/rfjMy1PFU7DDSfGMdCkd/e9C3ID5oujcUrorGxOsk7A+eARdmjmVTHJC8d+f1B1eW3zAoBpL4Jq7Z5KS460Jqip92FI78jwC7J97qiD++OvyY8X22Qc21Z80cBc8C/BFDfp1dFpj3jvTWyBVAOVXv0twz0w43hj1TjRO9pkwvPE0uTAxZ1B1wZfvYAclfZ1he2u/n7nerg6Y7R/i6R/ipYrOnUIEzWzhljfRJl0GeEv6jJkVmk7cJDzq38bOmcFXwdYaN6GyDCLmvVq3QfBkLaNFZkpwToW7wHw68UQOp3venwNGk1ySwONmv2TaCL40y9r9XftumTFMjCwM/FI+R68AbNp/PVAXYsMPxrgk2bnGvFsQvCO9c2ohHbbCR9ddz+RzOGbex5Su4K78nVP/VXL3puVgok1RiVPa3WZBM8jgJ3wpnFsoHp8AFNl6AMjK+1Vdoc7VaHtdFURt4D20bAupwAk35pU5oj7eSAccfgNw4gE9f1phSKvuvpyzqRaYMTp9+7oBzSFj7r96HhXZsiwm8dQOp3ven0dGHMnRWwoAWBf3MAubgFmTuy6wN0alF4T3AFQuqyhskZDPT67HjskCyvpfeFl3YL4zom9GQH09YYELOWMv1ePLJbgr4dvhotmD4656UZ81SYkxsiMz71I5q+pANbxCwOsxMM/MLwLmxrABs1fefoNHN88TonoseRa1YbrknQFFxaLMzdAeuog4/rBJD+dL0pT9fma4+vO+dzOuBp1wog/aGQFvPY8P05e6AccrN1ven81HHufq4KnSBo+B0DyMBx2DmfCa++yTopMq4aSL2u1z0NmN6JgfM5afCfCCCS5PyDwjOGjnqHhYfU6XpTnspfGNG5AfPZNBaEDwmoJtcY3xQ5+sC42BSLBVCd00mm89MdsmXs4kT91PxN+ku19XTYYBHzTa7zxzU9mtGcdq01o+MVZdCstTiTQ6yh7irh/xjUBPG18+IAUq/rTXnqA5vtJ4F+fmeKykhB1X1exrh6XcsC7tnmjLZWMfn02Zrgc4oz7sb6Q0hdHZsz+j8P6teHY8M8cz5JMWjX+UKbgI4nSf0jmj666n9uiH4TjTT6QlHOC84ItG0R6LsOPuBXXefG9OcNIOd0vWfw6e5/bdbAIcDu8OraKTxkgPA6N194P23rODqVHFspwgcCC0L7ULBqAKlX+Unp6Jcn6s7IFkUppC9d7/FP7fFC9v1U9SkWtEngGjX2bQajj/eriZ7ZRie/1BwjcI/sX8K/vi1vAHSd0+eb6uMaE8POJvC6C49qmuxWdzCiyp+aPvPwy1Oya3io+tFJHv+s454GNIxlxfC/DfTLovNOlKovyp7RVqf5Sroy6AfPi43dDakLT4A1SGR9Un+RDNwRaJuWYr8mgDypXa97Wsi3pIt+nXtt+k/2VddZeaKf+sTJ8b1RN8l9kS/TJuuDtmZY68LrZEVPB607kkLp5JMVoTpt8HzdkFBsugt/WBSH7ULXwcZFgQWhkPDbB9CwYnYjT3xqk6+wHwXYIUavaPpbBtC02O1mTOenRflQ7OIYgePow61/TFppWAIdszV43jPD/kyyaVN0XefV1rH3RahPFvRnkemHa9W5w3g3yF4ZxN6jATG68YZ9YGR45hDBGeDB4XWY9lcB9y30VIM3mdtmDjYI3GSeGV4ySfZrqw38uizAjjbguc4g9bre6E99XygqPw6w8Uhr63tDvQzNXKrJ+V5cwWGKsVRzq4430dDGwtJeEGCHXeh1eEn4pQOI4IfZrUnmS4w/w2OnFvbk9KWnmkTX4ofs6QE2OW2+k+q6Ec980iEc/FqwSnNptmlwzfv3zZlDwMC736LcG9k7A8k59Cu/Q/r+QHQuRdHm2kwI6XfXm/pT+/NHjRiXberKpz57Joz2d5u76qm44ulAlx+OJM47moRSGBPLGAnxzy4k1bX4UXZkgA5gDoVwrlfXYZFBdd1OMvWn66jV7ysfNy0BUph+cZ7MQXcqO9fkkUhPiMm1SPQvDVYPpNeG4dOWMQtY9ZF5Sjg9KweQa3e9J38Sm3E+ufY40JjWXYe+c74TnoSyk0kg/OTCI6dgJvqx8HxN+YnsXBLvaUDnpoAx4NwDw0PTWhDm6C/8LEcee35IPhiYVNqHAt5Hbwsco1DAhJtok+04/d8E6wacPOTInHdX+MUCSB+63rP8aTI+mzyYVJN2RMnNi8Nf0HS8ejgtJJq+V52J50fcUoF0WhgLou7ubVAf1H3WtiZikWTgzsCE0YJJATRn1wyeX8yTMYtCASoY2zOQ+JEJ8fuDMWDBeee8IID0pes9S59+Mu6W+EkUO9dksaMhX6LwLwl+GqBrQZgjT2EYOzlYNoCYD3Hv89vBgjhnewZDY3mXdDNnkac7kt8u/oCzGCSNP3FX2jGdR4JajKovb1F+H90tq4Hw2wXOV58i1aKl++wkd+R2CZ8kkUiT9PPwnp4Fwx8ToAP4SmJ3+7XEj0WSP2wM2SGB1xJ/ILy+6boWOlxnkGt2vWfR0x1Je2FAUkiQ18h24aENg8kB44xxPVkIZLzkVwheHvwyQAbQQdfTcmX4SQG0a4AO4xbF6zGiWfuTlgCHkadj4wyaRJPDHwS5xv6tN0ZB1GEOX2Vee2EHv9y/nrbaq3PgufbmD25sevxGQZ8xC/asOyX1RxUvXxJCot3N3wj/n03OmLudHU3/9uDNgUQCLTCydwW/DZxLsrFBH3w7OKrx9Zo8PDII/6qPA+Gs/DB5aydIi0DSSBaF4ccgPLL+FfXfkS0aQJyOekLgTeSy4c8ILIJFdb0HM2aBbZ/On1NibuKS74/DEgIJc/eaLGQkqe5sCvWxQBrtWqEgdWyv9C0KNiyyMlvG4D8fQPrY9Wbhp6eDX93sUpLgDjU5FIYiWaArw68VSNqwP6ytOhtE4aYA+xZE266pD3dHZ7EAqqevk8xiT3avO4+dSDJq4umTKHcr/UOCuQKIJDt/IHiSB7omdf7wxwYWgDVGK8rHMwbVk9ZJZrGnyeEd4J9JvK4shgm7PzpblfifTnLqXL6yHg5cpxbFU3Jdxv3dos/FlVmHNbhdWkL6xTA5P8r4qi1sTpXzmmhMTb3C1oiF8wI3gevSyu/QVqnFbKJZo/Gq4ffFtQHJqAVxd/K+4Je5RDGAX0/Kp6W1qNiRlgtzc4Afrg3vdXlReH22jWjWIXfa9gmJwHm5uiPdlSaEK2PPYPFAIqns8mkpDLquq51lwhwQ3BTUDTHMF/+cUk+XdiZ06w4jQf61lWRYCHZo7VMYcE9wYLByUIkkT60wrNcvBH9a+VrAu0n7tH5a0yr3lJwSmTS19dSZMK3JYccZdE2AMgpEMkC9RvjB9t1g7aBS/6uLQvR383qR8XX1WOA62HYdN4VjtNW3SelDfbuddAI+PR24zo4jYJJh0PuH3zrgmqpJMWG0ykkk/zFto6ASyarvB/hNg9MD57qudqv8F9HjvxZ9telbMHSOCKAaRyeZoE93Frvb3WiSuZIWanHxmblN4F9+TZgJ5EpTRntusEUwRyDNHWaH4JKg6mrDdR37afQ2C7SxYnhOI+Pq0l8lgIyl603AJ/eud+93wxuowfpnitl6sXG1eZpMHgVhXr8wfJXtGHw06J+yYXOQHR9sEFTyWj0sQtZEz7UOCg/VU9hJJtjTAFaO38N2HnIIPa4Ek4IMWic4PHgosDC0nDSK47VXx7huSKSn0TH+AesbwSuDSqzJpnFtTrJ23Tj8OWXxADKmrjfBnh5xvpRIDIkyyMPCS54i+sMKwxcSu/SGoCa6FoYkYruOI7s62C+w+GEHZCHsVx9OjhB/sYfP8BP+f6e4kxZNMHe1oCwGSZsUQBat6015In/elO6A+1Ce/X9TJ1mcCgAP3OEPhN8qqITNqa2J7saBtvSZ65D3HDQhX/AGzc4yaQZ36iCs7lF3JhLmWUz6swck9fzg0cBEadMdTB/ewnhS+LvVGcE7gppI1qjrpDsyjt4FATbx2QLvEB4ytq43AZ4Gyo7yzyQWgyDf2mLw3qbbLwT/GYGXdf13cuYCkl7t0bcAJtHCOIf2omD74PmBxLo1wfLbRc4c7LrWxeEtqm1E45/6QXm/E+DPAk8FhUO3Brdk+v8e3BjUZJIUUE8E19exAdff+gH/mlgLga7zqq1rIt85WCSQ8EF/kPEnf/SYZ0HgNwmgupk6yTh9mlxakm9AHvvtmt/998NLIv9ycG9g8kiuCXU+Y/cFBwcvC/q0RgTfDHh/aIe5w+zcEfkBwfJBpTlaZ9e02HA+/A/bGI2xFtH4Y905/JYwIe4wXozz9VxeJ/0jg/ppWwtRr6Jbord3sHRQiTVdVzk/8j4X3Bnoh4nFn3qS+CQ+NOh/Ei8Z2d0B840B/tUB5E3Q9cbh06sI13hxG4jB8+MNIhC+ZPy8NGGjXTFXRPcjwfyBxO7ETl0Tvi/jK2/3gM3gOr4X+oVh/eMCrj+JT2bjsCiHt0HWq+s7Z9y07tJJ8cidbRA3RLZMsHnAy9Xk1GBJSJWfm/4WgVdI2EHCueunlgjG6vuAeXxg7BDwYq5rsCY+9tc+JzI2DX826Z8Svtz8cwrrjEsiCSbp8PAETZAWhhfkJU1uQkhEPxlcKycFGwaVKPZYgu8XhhP09uAngX7Q6gttlV+Y/uQmwzcLN+7/nEKg0MqBfybxqiKQGqTBO87YgwGFXDuohN2xFKLagO8XBtl6wfeD+vvGE9MvjDHo8z2Zt3gATQ//OkvT8ekXx+dj0x1Xi1DvbU8N4wTGn1YoZCUKoc0qf7q8755q5xXpfC34XaDPFICieCKU47tF+UR4yM3Y9cbB0x2yWHzxvtVpArEYBkU7OSCgJQKJK4+rifaZpmFrLZNF9w1uC/RV3+smskjXR2/uADIHXW8mP90hO8cPAtFhd5nB0V4a7BjME0ju2hlRCNe0ZU38r2svmD5fhFcF+l4LA++G2zY8ZA663kx8ujMWiA/3BgTwWGBRDOjMyDYL/BIL+5S+mNCbEURBiMV4WHPOYOvg/MA4aInN984N4dGDnokrtrM8DU+d4DfCwcH9gc6zi44P1g8qURTnVfl44SlK3TgUi0/gUwNjo+X9t3/g38bqKYt45lF1ZKm4sXdwV7BTzyWO9XguRM/dwWnpX0V7RemOYLeAH51SzYGymdriUE02V5jBsNvGncPTkC3i8sTMHv6FZW694op4/LAUwULgVS3S+PFybJ7UWIhx3BejhomzE/lU1FgqT0wTqhDV+b/yf83AxM/A/wPXVNNrQzvOHAAAAABJRU5ErkJggg==") {
    id = "mutant-live-reload-connection-image"
}
