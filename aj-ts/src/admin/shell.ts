// 判断当前页面是否在 iframe 中 
if (self != top)
    parent.window.location.reload();

// 收缩菜单
function hideSider(el: HTMLElement): void {
    //@ts-ignore
    if (hideSider.isHidden) {
        //@ts-ignore
        document.querySelector('.side').style.width = '20%';
        //@ts-ignore
        document.querySelector('.iframe').style.width = '80%';
        //@ts-ignore
        hideSider.isHidden = false;
        el.style.right = '0';
    } else {
        //@ts-ignore
        document.querySelector('.side').style.width = '0';
        //@ts-ignore
        document.querySelector('.iframe').style.width = '100%';
        //@ts-ignore
        hideSider.isHidden = true;
        el.style.right = '-' + el.clientWidth + 'px';
    }
}

// 初始化菜单
new Vue({ el: '.menu' });

// 需要把链接配置属性  target="iframepage"
// 获取 # target=abc 参数
function getTarget(): string {
    let target = window.location.hash.match(/target=([^$]+)/);
    //@ts-ignore
    return target && target.pop();
}

// 展开菜单
function highlightMenu(target: string): void {
    let a = document.querySelector(`a[href="${target}"]`);
    if (a) {
        let li = a.up('li'), ul = <HTMLElement>li?.up('ul');
        if (li) {
            li.classList.add('selected');
            ul.style.height = 'auto';
            ul.up('li')?.classList.add('pressed');
        }
    }
}

function logout(): void {
    aj.showConfirm('确定退出吗？', () => aj.xhr.get(`${aj.ctx}/user/logout/`, (j: RepsonseResult) => {
        if (j.isOk) {
            aj.msg.show(j.msg);
            setTimeout(() => location.assign(`${aj.ctx}/admin/login/`), 1000);
        }
    }));
}

let prefix: string = location.origin + "",
    iframepageName: string = 'iframepage',
    target = getTarget(),
    iframeEl: HTMLIFrameElement = <HTMLIFrameElement>document.querySelector(`iframe[name=${iframepageName}]`);

if (target && iframeEl) {
    iframeEl.src = target; // 跳转 iframe
    highlightMenu(target);
}

// 点击菜单时保存按钮
document.body.$(`a[target=${iframepageName}]`, (a: HTMLAnchorElement) => a.onclick = addHash);

function addHash(ev: Event): boolean {
    // let target:string = new String(iframeEl.contentWindow?.location);
    let target: string | null = (<Element>ev.target).getAttribute('href');

    window.location.assign('#target=' + target); // 为主窗体添加描点记录，以便 F5 刷新可以回到这里

    return false; // onhashchange() 里面已经跳转了，这里避免 a 再次跳转
}

window.onhashchange = (ev: Event) => {
    let target = getTarget();
    iframeEl.src = target;
    //	        	highlightMenu(target);
}