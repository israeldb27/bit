import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Empresa e2e test', () => {

    let navBarPage: NavBarPage;
    let empresaDialogPage: EmpresaDialogPage;
    let empresaComponentsPage: EmpresaComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Empresas', () => {
        navBarPage.goToEntity('empresa');
        empresaComponentsPage = new EmpresaComponentsPage();
        expect(empresaComponentsPage.getTitle()).toMatch(/bcbApp.empresa.home.title/);

    });

    it('should load create Empresa dialog', () => {
        empresaComponentsPage.clickOnCreateButton();
        empresaDialogPage = new EmpresaDialogPage();
        expect(empresaDialogPage.getModalTitle()).toMatch(/bcbApp.empresa.home.createOrEditLabel/);
        empresaDialogPage.close();
    });

    it('should create and save Empresas', () => {
        empresaComponentsPage.clickOnCreateButton();
        empresaDialogPage.setRazaoSocialInput('razaoSocial');
        expect(empresaDialogPage.getRazaoSocialInput()).toMatch('razaoSocial');
        empresaDialogPage.setNomeInput('nome');
        expect(empresaDialogPage.getNomeInput()).toMatch('nome');
        empresaDialogPage.setDataCadastroInput('2000-12-31');
        expect(empresaDialogPage.getDataCadastroInput()).toMatch('2000-12-31');
        empresaDialogPage.setDataUltimaAtualizacaoInput('2000-12-31');
        expect(empresaDialogPage.getDataUltimaAtualizacaoInput()).toMatch('2000-12-31');
        empresaDialogPage.setCnpjInput('5');
        expect(empresaDialogPage.getCnpjInput()).toMatch('5');
        empresaDialogPage.statusSelectLastOption();
        empresaDialogPage.setQuantidadeAcoesInput('5');
        expect(empresaDialogPage.getQuantidadeAcoesInput()).toMatch('5');
        empresaDialogPage.setValorInicialInput('5');
        expect(empresaDialogPage.getValorInicialInput()).toMatch('5');
        empresaDialogPage.save();
        expect(empresaDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class EmpresaComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-empresa div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EmpresaDialogPage {
    modalTitle = element(by.css('h4#myEmpresaLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    razaoSocialInput = element(by.css('input#field_razaoSocial'));
    nomeInput = element(by.css('input#field_nome'));
    dataCadastroInput = element(by.css('input#field_dataCadastro'));
    dataUltimaAtualizacaoInput = element(by.css('input#field_dataUltimaAtualizacao'));
    cnpjInput = element(by.css('input#field_cnpj'));
    statusSelect = element(by.css('select#field_status'));
    quantidadeAcoesInput = element(by.css('input#field_quantidadeAcoes'));
    valorInicialInput = element(by.css('input#field_valorInicial'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setRazaoSocialInput = function (razaoSocial) {
        this.razaoSocialInput.sendKeys(razaoSocial);
    }

    getRazaoSocialInput = function () {
        return this.razaoSocialInput.getAttribute('value');
    }

    setNomeInput = function (nome) {
        this.nomeInput.sendKeys(nome);
    }

    getNomeInput = function () {
        return this.nomeInput.getAttribute('value');
    }

    setDataCadastroInput = function (dataCadastro) {
        this.dataCadastroInput.sendKeys(dataCadastro);
    }

    getDataCadastroInput = function () {
        return this.dataCadastroInput.getAttribute('value');
    }

    setDataUltimaAtualizacaoInput = function (dataUltimaAtualizacao) {
        this.dataUltimaAtualizacaoInput.sendKeys(dataUltimaAtualizacao);
    }

    getDataUltimaAtualizacaoInput = function () {
        return this.dataUltimaAtualizacaoInput.getAttribute('value');
    }

    setCnpjInput = function (cnpj) {
        this.cnpjInput.sendKeys(cnpj);
    }

    getCnpjInput = function () {
        return this.cnpjInput.getAttribute('value');
    }

    setStatusSelect = function (status) {
        this.statusSelect.sendKeys(status);
    }

    getStatusSelect = function () {
        return this.statusSelect.element(by.css('option:checked')).getText();
    }

    statusSelectLastOption = function () {
        this.statusSelect.all(by.tagName('option')).last().click();
    }
    setQuantidadeAcoesInput = function (quantidadeAcoes) {
        this.quantidadeAcoesInput.sendKeys(quantidadeAcoes);
    }

    getQuantidadeAcoesInput = function () {
        return this.quantidadeAcoesInput.getAttribute('value');
    }

    setValorInicialInput = function (valorInicial) {
        this.valorInicialInput.sendKeys(valorInicial);
    }

    getValorInicialInput = function () {
        return this.valorInicialInput.getAttribute('value');
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
