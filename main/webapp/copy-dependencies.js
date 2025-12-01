import fs from 'fs';
import path from 'path';

const FROM_DIR = path.join('node_modules');
const TO_DIR = path.join('modules', 'core', '3rdparty');

if (!fs.existsSync(TO_DIR)) {
    fs.mkdirSync(TO_DIR);
    console.log('Directory '+TO_DIR+' created.');
}

try {
    const data = fs.readFileSync('dependencies.json', 'utf8');
    const dependencies = JSON.parse(data);

    const DIRS = dependencies.directories;
    for (const dir of DIRS) {
        const dirPath = path.join(TO_DIR, dir);
        if (!fs.existsSync(dirPath)) {
            fs.mkdirSync(dirPath);
            console.log('Directory '+dirPath+' created.');
        }
    }

    // paths are relative to the FROM_DIR and TO_DIR
    const PATHS = dependencies.files;

    for (const item of PATHS) {
        const from = item.from;
        const fromPath = path.join(FROM_DIR, from);
        const to = item.to === '' ? from : item.to;
        const toPath = path.join(TO_DIR, to);

        if (fs.lstatSync(fromPath).isDirectory()) { // copy directory
            console.log(`Copy all files of folder ${fromPath}`);
            copyFolderRecursively(fromPath, toPath);
        } else { // copy single file
            fs.copyFileSync(fromPath, toPath);
            console.log(`${fromPath} was copied to ${toPath}`);
        }
    }

} catch (err) {
    console.log(`Error reading dependencies.json: ${err}`);
    process.exit(-1);
}

function copyFolderRecursively(from, to) {
    if (!fs.existsSync(to)) {
        fs.mkdirSync(to);
    }

    const entries = fs.readdirSync(from, { withFileTypes: true });
    for (const entry of entries) {
        const fromPath = path.join(from, entry.name);
        const toPath = path.join(to, entry.name);

        if (entry.isFile()) {  // end condition
            fs.copyFileSync(fromPath, toPath);
            console.log(`- ${fromPath} was copied to ${toPath}`);
        } else if (entry.isDirectory()) {   // recursive
            copyFolderRecursively(fromPath, toPath);
        }
    }
}
