#!/usr/bin/python

from bs4 import BeautifulSoup
from bs4.element import Tag
from bs4.element import NavigableString
from bs4.element import Comment
import os
import codecs
import lxml.etree as etree
import lxml.builder
import logging
import logging.handlers

input_path = "/Users/hector/Sites/wikihow/www.wikihow.com"
output_base_path = "/Users/hector/Documents/data/wikihow_xml"


def is_category_link(tag):
    return tag.name == 'a' and tag.has_attr('title') and tag['title'] != "Main Page" and not tag['title'].startswith(
        "Special")


def is_steps_list_class(s):
    return s is None or s.startswith('steps_list')


def is_clear_div(tag):
    return tag.name == 'div' and tag.has_attr('class') and tag['class'][0] == 'clearall'


def parse_ul_text(tag):
    ls = etree.Element('ul')
    for l in tag(lambda t: t.name == 'li'):
        li = etree.SubElement(ls, "li")
        li.text = l.get_text()

    return ls


def create_text_node(s,name = None):
    if not s or s.isspace():
        return None
    n = etree.Element('text')
    n.text = s
    if not name is None:
        n.set('n',name)
    return n


def parse_text(tag):
    #get immediate text form this tag, do not go deeper
    if type(tag) is NavigableString:
        if not type(tag) is Comment: 
            return create_text_node(unicode(tag.string))
    elif type(tag) is Tag:
        if tag.name == 'ul':
            return parse_ul_text(tag)
        elif tag.name == 'a':
            ref = tag['href'] if tag.has_attr('href') else ''
            title = tag['title'] if tag.has_attr('title') else ''
            anchor = etree.Element('a')
            anchor.set('href', ref)
            anchor.set('title', title)
            anchor.text = tag.get_text()
            return anchor
        elif tag.name == 'div':
            if len(tag('script')) > 0:
                return None
            elif tag.has_attr('class'):
                for c in tag['class']:
                    if c.startswith('ad_label'):
                        return None
                else:
                    return create_text_node(tag.get_text())
            else:
                return create_text_node(tag.get_text())
        else:
            return create_text_node(tag.get_text(),tag.name)


def get_text_from_tag_group(tags):
    #get text for each tag
    s = []
    last_tag_name = None
    for tag in tags:
        current_tag_name = 'NavigableString' if type(tag) is NavigableString else tag.name
        if type(tag) is NavigableString:
            n = parse_text(tag)
            if n is not None:
                s.append(n)
        elif tag.name != 'script': 
            n = parse_text(tag)
            s.append(n)
    return s


def get_all_children_text(tag):
    return get_text_from_tag_group(tag.children)


def parse_category(tag):
    c_nodes = []
    for a in tag(is_category_link):
        c = etree.Element('c')
        c.text = a.get_text()
        c_nodes.append(c)
    return c_nodes


def parse_categories(category_tags):
    categories = etree.Element('categories')

    for cs in category_tags:
        category = etree.SubElement(categories, 'category')
        for c in parse_category(cs):
            category.append(c)

    return categories


def parse_step_details(tag):
    tags_to_parse = []
    while not tag is None and not is_clear_div(tag):
        tags_to_parse.append(tag)
        tag = tag.next_sibling
    return get_text_from_tag_group(tags_to_parse)


def parse_step_listing(tag):
    steps = []
    for step_em_tag in tag('b', class_='whb'):
        step = etree.Element('step')
        em = etree.SubElement(step, 'em')
        em.text = step_em_tag.get_text()

        details = etree.SubElement(step, 'details')
        for detail in parse_step_details(step_em_tag.next_sibling):
            details.append(detail)

        steps.append(step)

    return steps


def parse_step_listings(step_listings):
    methods = etree.Element('methods')

    for listing in step_listings:
        method = etree.SubElement(methods, 'method')
        for s in parse_step_listing(listing):
            method.append(s)
    return methods


def parse_listed_point(tag):
    tips = []
    for tip in tag.children:
        n = parse_text(tip)
        if n is not None:
            tips.append(n)
    return tips


def parse_listed_points(tips_divs,group_name,item_name):
    tips_node = etree.Element(group_name)
    for tips in tips_divs:
        for tip in parse_listed_point(tips):
            tip_node = etree.SubElement(tips_node, item_name)
            tip_node.append(tip)
    return tips_node


def parse_warnings(warnings_divs):
    warnings_node = etree.Element('warnings')
    for warnings in warnings_divs:
        for warning in parse_warning(warnings):
            warning_node = etree.SubElement(warnings_node, 'warning')
            warning_node.append(warning)
    return warnings_node


def parse_related_wikihow(related_wikihow_tags):
    related_pages_node = etree.Element('related_pages')
    for related_wikihow_tag in related_wikihow_tags:
        for related_wikihow_text in related_wikihow_tag('div', class_='text'):
            related_wikihow_anchor = related_wikihow_text.parent
            href = ''
            if not related_wikihow_anchor is None:
                if type(related_wikihow_anchor) == Tag and related_wikihow_anchor.name == 'a':
                    if related_wikihow_anchor.has_attr('href'):
                        href = related_wikihow_anchor['href']
            
            related_page_node = etree.SubElement(related_pages_node,'a')
            related_page_node.text = unicode(related_wikihow_text.get_text())
            related_page_node.set('href',href)

    return related_pages_node


def parse_meta(metas):
    meta_node = etree.Element('meta')
    for meta in metas:
        if meta.has_attr('name'):
            if meta['name'] == 'description':
                etree.SubElement(meta_node, 'description').text = meta['content'] if meta.has_attr('content') else ''
            if meta['name'] == 'keywords':
                etree.SubElement(meta_node, 'keywords').text = meta['content'] if meta.has_attr('content') else ''
        if meta.has_attr('property'):
            if meta['property'] == 'og:type':
                etree.SubElement(meta_node, 'type').text = meta['content'] if meta.has_attr('content') else ''
            if meta['property'] == 'og:url':
                etree.SubElement(meta_node, 'url').text =  meta['content'] if meta.has_attr('content') else ''
            if meta['property'] == 'og:title':
                etree.SubElement(meta_node, 'title').text = meta['content'] if meta.has_attr('content') else ''

    return meta_node


def parse_alternate_links(links):
    links_node = etree.Element('alter_lang')
    for link in links:
        if link.has_attr('rel') and link.has_attr('hreflang') and link.has_attr('href'):
            lang_node = etree.SubElement(links_node,'lang')
            lang_node.text = link['hreflang']
            lang_node.set('href',link['href'])
    return links_node
   

def parse_general_description(ps):
    g_node = etree.Element('general')
    for p in ps:
        if p.has_attr('id') and p['id'] == 'method_toc':
            toc = etree.SubElement(g_node,'method_toc')
            for t in get_all_children_text(p):
                toc.append(t)
        else:
            summary = etree.SubElement(g_node,'summary')
            for t in get_all_children_text(p):
                summary.append(t)
    return g_node


def find_general_paragraphs(soup):
    nodes =  soup.select("div.clearall.social_proof")
    general_paras = []
    if len(nodes) == 1:
        n = nodes[0]
        while not n.next_sibling is None:
            n = n.next_sibling
            if n.name == 'p':
                general_paras.append(n)
            else:
                if n.name == "div" and n.has_attr('class'):
                        if 'clearall' in n['class']:
                            break
    return general_paras


def parse_wiki_how(wikihow_page):
    soup = BeautifulSoup(wikihow_page)
    
    # get rid of irrelevant articles by type
    meta = parse_meta(soup('meta'))
    article_type  = meta.xpath("type")
    if article_type and article_type[0].text == 'article':
        pass
    else:
        return None

    categories = parse_categories(soup('ul', id='breadcrumb'))
    methods = parse_step_listings(soup('ol', class_=is_steps_list_class))
    tips = parse_listed_points(soup('div', id='tips'),'tips','tip')
    warnings = parse_listed_points(soup('div', id='warnings'),'warnings','warning')
    things_you_ll_need = parse_listed_points(soup('div',id='thingsyoullneed'),'things','thing')
    ingredients = parse_listed_points(soup('div',id='ingredients'),'ingredients','igredient')
    related = parse_related_wikihow(soup('div', id='relatedwikihows'))
    alternatives = parse_alternate_links(soup('link'))
    general = parse_general_description(find_general_paragraphs(soup))

    root = etree.Element('root')
    doc = etree.SubElement(root, 'document')
    doc.append(meta)
    doc.append(alternatives)
    doc.append(categories)
    doc.append(general)
    doc.append(methods)
    doc.append(tips)
    doc.append(warnings)
    doc.append(things_you_ll_need)
    doc.append(ingredients)
    doc.append(related)
    return root


def create_category_dir(categories):
    path_seg = []
    for category in categories.iter():
        c = category.text
        if not c is None:
            path_seg.append(category.text)
    return os.path.join(path_seg)


class warningFilter(logging.Filter):
    def filter(self,record):
        return record.levelno == logging.WARNING

def main():
    if not os.path.exists(output_base_path):
        os.makedirs(output_base_path)
   
    logger = logging.getLogger('wikihow_parser_logger')
    formatter = logging.Formatter('%(asctime)s | %(name)s |  %(levelname)s: %(message)s')
    logger.setLevel(logging.WARNING)

    logFilePath = 'parsing_error.log'
    errorLogger = logging.handlers.TimedRotatingFileHandler(filename = logFilePath, when = 'midnight', backupCount = 30)
    errorLogger.setFormatter(formatter)
    errorLogger.setLevel(logging.ERROR)

    logFilePath = 'parsing_warning.log'
    warningLogger = logging.handlers.TimedRotatingFileHandler(filename = logFilePath, when = 'midnight', backupCount = 30)
    warningLogger.setFormatter(formatter)
    warningLogger.setLevel(logging.WARNING)
    warningLogger.addFilter(warningFilter())

    logger.addHandler(errorLogger)
    logger.addHandler(warningLogger)

    for wikihow_page_path in os.listdir(input_path):
        try:
            if not wikihow_page_path.endswith(".html"):
                continue
            wikihow_page = open(os.path.join(input_path, wikihow_page_path))
            parse_result = parse_wiki_how(wikihow_page)
            if parse_result is None:
                logger.warning('['+wikihow_page_path + '] might not be a wikihow page, not parsed')
                #might not be a wikihow article
                continue
            et = etree.ElementTree(parse_result)
            subpath = ''
            categories = et.xpath("//categories")
            if categories:
                subpath = os.path.join('',*create_category_dir(categories[0]))
            output_path = os.path.join(output_base_path,subpath)
            if not os.path.exists(output_path):
                os.makedirs(output_path)
            et.write(os.path.join(output_path, wikihow_page_path + ".xml"), xml_declaration=True, encoding='utf-8',
                     pretty_print=True)
        except Exception:
            logger.exception('Error when processing ' + wikihow_page_path)  

if __name__ == "__main__":
    main()
